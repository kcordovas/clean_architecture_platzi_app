package com.platzi.android.rickandmorty.dependencyinyect

import com.platzi.android.rickandmorty.presentation.FavoriteListViewModel
import com.platzi.android.rickandmorty.usescases.GetAllFavoriteCharacterUseCase
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class FavoriteListModule {
    @Provides
    fun favoriteListViewModelProvider(
        getAllFavoriteCharacterUseCase: GetAllFavoriteCharacterUseCase
    ) = FavoriteListViewModel(
        getAllFavoriteCharacterUseCase
    )
}

@Subcomponent(modules = [(FavoriteListModule::class)])
interface FavoriteListComponent {
    val favoriteListViewModel: FavoriteListViewModel
}