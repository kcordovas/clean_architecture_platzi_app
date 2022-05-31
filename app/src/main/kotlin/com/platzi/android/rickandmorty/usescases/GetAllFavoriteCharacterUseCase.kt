package com.platzi.android.rickandmorty.usescases

import com.platzi.android.rickandmorty.database.CharacterDao
import com.platzi.android.rickandmorty.database.CharacterEntity
import com.platzi.android.rickandmorty.database.toCharacterDomainList
import io.reactivex.schedulers.Schedulers

class GetAllFavoriteCharacterUseCase(
    private val characterDao: CharacterDao
) {

    fun invoke() = characterDao.getAllFavoriteCharacters()
        .map(List<CharacterEntity>::toCharacterDomainList)
        .onErrorReturn { emptyList() }
        .subscribeOn(Schedulers.io())
}