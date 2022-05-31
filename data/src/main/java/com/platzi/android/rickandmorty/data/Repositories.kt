package com.platzi.android.rickandmorty.data

import com.platzi.android.rickandmorty.domain.Character

class CharacterRepository(
    private val remoteCharacterDataSource: RemoteCharacterDataSource,
    private val localCharacterDataSource: LocalCharacterDataSource
) {
    fun getAllCharacters(page: Int) = remoteCharacterDataSource.getAllCharacters(page)
    fun getAllFavoriteCharacters() = localCharacterDataSource.getFavoriteCharacters()
    fun getFavoriteCharacterStatus(id : Int) = localCharacterDataSource.getFavoriteCharacterStatus(id)
    fun updateFavoriteCharacterStatus(character: Character) = localCharacterDataSource.updateFavoriteCharacterStatus(character)
}