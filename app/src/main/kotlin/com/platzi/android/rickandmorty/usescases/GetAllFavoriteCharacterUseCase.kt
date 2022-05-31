package com.platzi.android.rickandmorty.usescases

import com.platzi.android.rickandmorty.data.CharacterRepository

class GetAllFavoriteCharacterUseCase(
    private val characterRepository: CharacterRepository
) {

    fun invoke() = characterRepository.getAllFavoriteCharacters()
}