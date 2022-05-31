package com.platzi.android.rickandmorty

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.platzi.android.rickandmorty.dependencyinyect.DaggerRickAndMortyPlatziComponent
import com.platzi.android.rickandmorty.dependencyinyect.RickAndMortyPlatziComponent

class RickAndMortyPlatziApp: Application() {

    lateinit var component: RickAndMortyPlatziComponent

    //region Override Methods & Callbacks

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        component = initAppComponent()
    }

    //endregion

    private fun initAppComponent() = DaggerRickAndMortyPlatziComponent.factory().create(this)

}
