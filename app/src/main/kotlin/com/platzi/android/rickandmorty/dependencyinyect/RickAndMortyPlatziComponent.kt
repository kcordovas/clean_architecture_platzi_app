package com.platzi.android.rickandmorty.dependencyinyect

import android.app.Application
import com.platzi.android.rickandmorty.data.RepositoryModule
import com.platzi.android.rickandmorty.framework.databasemanager.DatabaseModule
import com.platzi.android.rickandmorty.framework.requestmanager.ApiModule
import com.platzi.android.rickandmorty.usescases.UseCaseModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, DatabaseModule::class, RepositoryModule::class, UseCaseModule::class])
interface RickAndMortyPlatziComponent {
    fun inject(module: CharacterListModule) : CharacterListComponent
    fun inject(module: FavoriteListModule) : FavoriteListComponent
    fun inject(module: CharacterDetailModule) : CharacterDetailComponent

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application) : RickAndMortyPlatziComponent
    }
}