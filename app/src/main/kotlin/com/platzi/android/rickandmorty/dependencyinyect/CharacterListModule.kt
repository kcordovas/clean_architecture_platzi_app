package com.platzi.android.rickandmorty.dependencyinyect

import com.platzi.android.rickandmorty.presentation.CharacterListViewModel
import com.platzi.android.rickandmorty.usescases.GetAllCharactersUseCase
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class CharacterListModule {
    @Provides
    fun characterListViewModelProvider(
        getAllCharactersUseCase: GetAllCharactersUseCase
    ) = CharacterListViewModel(
        getAllCharactersUseCase
    )
}

@Subcomponent(modules = [(CharacterListModule::class)])
interface CharacterListComponent {
    val characterListViewModel : CharacterListViewModel
}
