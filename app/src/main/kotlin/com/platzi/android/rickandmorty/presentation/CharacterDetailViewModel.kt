package com.platzi.android.rickandmorty.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.platzi.android.rickandmorty.api.*
import com.platzi.android.rickandmorty.database.CharacterDao
import com.platzi.android.rickandmorty.database.CharacterEntity
import com.platzi.android.rickandmorty.usescases.GetEpisodeFromCharacterUseCase
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CharacterDetailViewModel(
    private val characterDao: CharacterDao,
    private val getEpisodeFromCharacterUseCase: GetEpisodeFromCharacterUseCase,
    private val character: CharacterServer? = null
) : ViewModel() {
    private val disposable = CompositeDisposable()

    private val _characterValues = MutableLiveData<CharacterServer>()
    val characterValues: LiveData<CharacterServer> get() = _characterValues

    private val _isFavoriteCharacter = MutableLiveData<Boolean>()
    val isFavoriteCharacter: LiveData<Boolean> get() = _isFavoriteCharacter

    private val _events = MutableLiveData<Event<CharacterDetailsNavigation>>()
    val events: LiveData<Event<CharacterDetailsNavigation>> get() = _events

    sealed class CharacterDetailsNavigation {
        data class ShowCharacterDetailsError(val throwable: Throwable) :
            CharacterDetailsNavigation()

        data class ShowEpisodesServerList(val listEpisodeServer: List<EpisodeServer>) :
            CharacterDetailsNavigation()

        object ShowLoading : CharacterDetailsNavigation()
        object HideLoading : CharacterDetailsNavigation()
        object CloseActivity : CharacterDetailsNavigation()
    }

    fun onCharacterValidation() {
        if (character == null) {
            _events.value = Event(CharacterDetailsNavigation.CloseActivity)
            return
        }

        _characterValues.value = character
        onValidateFavoriteCharacterStatus()
        onShowEpisodeList(characterValues.value!!.episodeList)
    }

    private fun onValidateFavoriteCharacterStatus() {
        disposable.add(
            characterDao.getCharacterById(character!!.id)
                .isEmpty
                .flatMapMaybe { isEmpty ->
                    Maybe.just(!isEmpty)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { isFavorite ->
                    _isFavoriteCharacter.value = isFavorite
                }
        )
    }

    private fun onShowEpisodeList(episodeUrlList: List<String>) {
        disposable.add(
            getEpisodeFromCharacterUseCase.invoke(episodeUrlList)
                .doOnSubscribe {
                    _events.value = Event(CharacterDetailsNavigation.ShowLoading)
                }
                .subscribe(
                    { episodeList ->
                        _events.value = Event(CharacterDetailsNavigation.HideLoading)
                        _events.value =
                            Event(CharacterDetailsNavigation.ShowEpisodesServerList(episodeList))
                    },
                    { error ->
                        _events.value = Event(CharacterDetailsNavigation.HideLoading)
                        _events.value =
                            Event(CharacterDetailsNavigation.ShowCharacterDetailsError(error))
                    })
        )
    }

    fun onUpdateFavoriteCharacterStatus() {
        val characterEntity: CharacterEntity = character!!.toCharacterEntity()
        disposable.add(
            characterDao.getCharacterById(characterEntity.id)
                .isEmpty
                .flatMapMaybe { isEmpty ->
                    if (isEmpty) {
                        characterDao.insertCharacter(characterEntity)
                    } else {
                        characterDao.deleteCharacter(characterEntity)
                    }
                    Maybe.just(isEmpty)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { isFavorite ->
                    _isFavoriteCharacter.value = isFavorite
                }
        )
    }


    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}