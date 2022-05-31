package com.platzi.android.rickandmorty.usescases

import com.platzi.android.rickandmorty.data.CharacterRepository
import com.platzi.android.rickandmorty.database.CharacterDao
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class GetFavoriteCharacterStatusCase(
    private val characterRepository: CharacterRepository
) {
    fun invoke(characterId: Int) : Maybe<Boolean> = characterRepository.getFavoriteCharacterStatus(characterId)
}