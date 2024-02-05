package com.platzi.android.rickandmorty.framework.databasemanager

import com.platzi.android.rickandmorty.data.LocalCharacterDataSource
import com.platzi.android.rickandmorty.domain.Character
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CharacterRoomDataSource(
    database: CharacterDatabase
) : LocalCharacterDataSource {
    private val characterDao by lazy {
        database.characterDao()
    }
    override fun getFavoriteCharacters(): Flowable<List<Character>> =
        characterDao.getAllFavoriteCharacters()
            .map(List<CharacterEntity>::toCharacterDomainList)
            .onErrorReturn { emptyList() }
            .subscribeOn(Schedulers.io())

    override fun getFavoriteCharacterStatus(id: Int): Maybe<Boolean> =
        characterDao.getCharacterById(id)
            .isEmpty
            .flatMapMaybe { isEmpty ->
                Maybe.just(!isEmpty)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())

    override fun updateFavoriteCharacterStatus(character: Character): Maybe<Boolean> = characterDao.getCharacterById(character.id)
        .isEmpty
        .flatMapMaybe { isEmpty ->
            if (isEmpty) {
                characterDao.insertCharacter(character.toCharacterEntity())
            } else {
                characterDao.deleteCharacter(character.toCharacterEntity())
            }
            Maybe.just(isEmpty)
        }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())

}