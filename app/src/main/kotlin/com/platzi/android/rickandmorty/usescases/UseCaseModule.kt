package com.platzi.android.rickandmorty.usescases

import com.platzi.android.rickandmorty.data.CharacterRepository
import com.platzi.android.rickandmorty.data.EpisodeRepository
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {

    @Provides
    fun getAllCharacterUseCaseProvider(characterRepository: CharacterRepository) = GetAllCharactersUseCase(characterRepository)

    @Provides
    fun getAllFavoriteCharacterUseCaseProvider(characterRepository: CharacterRepository) = GetAllFavoriteCharacterUseCase(characterRepository)

    @Provides
    fun getFavoriteCharacterStatusUseCaseProvider(characterRepository: CharacterRepository) = GetFavoriteCharacterStatusCase(characterRepository)

    @Provides
    fun updateFavoriteCharacterStatusUseCaseProvider(characterRepository: CharacterRepository) = UpdateFavoriteCharacterStatusUseCase(characterRepository)

    @Provides
    fun getEpisodeFromCharacterUseCaseProvider(episodeRepository: EpisodeRepository) = GetEpisodeFromCharacterUseCase(episodeRepository)
}