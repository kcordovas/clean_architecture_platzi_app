package com.platzi.android.rickandmorty.usescases

import com.platzi.android.rickandmorty.data.CharacterRepository
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {

    @Provides
    fun getAllCharacterUseCaseProvider(characterRepository: CharacterRepository) = GetAllCharactersUseCase(characterRepository)

    @Provides
    fun getAllFavoriteCharacterUseCaseProvider(characterRepository: CharacterRepository) = GetAllFavoriteCharacterUseCase(characterRepository)
}