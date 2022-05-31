package com.platzi.android.rickandmorty.data

class CharacterRepository(
    private val remoteCharacterDataSource: RemoteCharacterDataSource
) {
    fun getAllCharacters(page: Int) = remoteCharacterDataSource.getAllCharacters(page)
}