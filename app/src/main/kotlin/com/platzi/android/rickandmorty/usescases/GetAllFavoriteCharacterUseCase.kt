package com.platzi.android.rickandmorty.usescases

import com.platzi.android.rickandmorty.database.CharacterDao
import io.reactivex.schedulers.Schedulers

class GetAllFavoriteCharacterUseCase(
    private val characterDao: CharacterDao
) {

    fun invoke() = characterDao.getAllFavoriteCharacters()
        .onErrorReturn { emptyList() }
        .subscribeOn(Schedulers.io())
}