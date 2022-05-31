package com.platzi.android.rickandmorty.usescases

import com.platzi.android.rickandmorty.database.CharacterDao
import com.platzi.android.rickandmorty.database.toCharacterEntity
import com.platzi.android.rickandmorty.domain.Character
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UpdateFavoriteCharacterStatusUseCase(
    private val characterDao: CharacterDao
) {

    fun invoke(character: Character) : Maybe<Boolean> = characterDao.getCharacterById(character.id)
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