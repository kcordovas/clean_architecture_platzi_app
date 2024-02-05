package com.platzi.android.rickandmorty.usescases

import com.platzi.android.rickandmorty.data.CharacterRepository
import io.reactivex.Maybe

class GetFavoriteCharacterStatusCase(
    private val characterRepository: CharacterRepository
) {
    fun invoke(characterId: Int) : Maybe<Boolean> = characterRepository.getFavoriteCharacterStatus(characterId)
}