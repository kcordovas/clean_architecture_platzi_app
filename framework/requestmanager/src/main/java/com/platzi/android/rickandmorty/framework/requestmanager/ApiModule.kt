package com.platzi.android.rickandmorty.framework.requestmanager

import com.platzi.android.rickandmorty.data.RemoteCharacterDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    fun characterRequestProvider(
        @Named("baseUrl") baseUrl: String,
    ) = CharacterRequest(baseUrl)

    @Provides
    @Singleton
    @Named("baseUrl")
    fun baseUrlProvider() : String = APIConstants.BASE_API_URL

    @Provides
    fun remoteCharacterDataSourceProvider(
        characterRequest: CharacterRequest
    ) : RemoteCharacterDataSource = CharacterRetrofitDataSource(characterRequest)
}