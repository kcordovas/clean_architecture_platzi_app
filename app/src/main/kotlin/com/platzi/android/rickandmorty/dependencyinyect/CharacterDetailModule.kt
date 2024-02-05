package com.platzi.android.rickandmorty.dependencyinyect

import com.platzi.android.rickandmorty.domain.Character
import com.platzi.android.rickandmorty.presentation.CharacterDetailViewModel
import com.platzi.android.rickandmorty.usescases.GetEpisodeFromCharacterUseCase
import com.platzi.android.rickandmorty.usescases.GetFavoriteCharacterStatusCase
import com.platzi.android.rickandmorty.usescases.UpdateFavoriteCharacterStatusUseCase
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class CharacterDetailModule(
    private val character: Character?
) {

    @Provides
    fun characterDetailViewModelProvider(
        getEpisodeFromCharacterUseCase: GetEpisodeFromCharacterUseCase,
        getFavoriteCharacterStatusUseCase: GetFavoriteCharacterStatusCase,
        updateFavoriteCharacterStatusUseCase: UpdateFavoriteCharacterStatusUseCase
    ) = CharacterDetailViewModel(
        character,
        getEpisodeFromCharacterUseCase,
        getFavoriteCharacterStatusUseCase,
        updateFavoriteCharacterStatusUseCase
    )
}

@Subcomponent(modules = [(CharacterDetailModule::class)])
interface CharacterDetailComponent {
    val characterDetailViewModel : CharacterDetailViewModel
}