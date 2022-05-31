package com.platzi.android.rickandmorty.usescases

import com.platzi.android.rickandmorty.data.CharacterRepository

class GetAllCharactersUseCase(
    private val characterRepository: CharacterRepository
) {

    fun invoke(currentPage: Int) = characterRepository.getAllCharacters(currentPage)
}