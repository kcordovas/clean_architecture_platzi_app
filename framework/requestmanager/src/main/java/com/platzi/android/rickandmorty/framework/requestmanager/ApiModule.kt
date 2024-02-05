package com.platzi.android.rickandmorty.framework.requestmanager

import com.platzi.android.rickandmorty.data.RemoteCharacterDataSource
import com.platzi.android.rickandmorty.data.RemoteEpisodeDataSource
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

    @Provides
    fun episodeRequestProvider(
        @Named("baseUrl") baseUrl: String
    ) = EpisodeRequest(baseUrl)

    @Provides
    fun remoteEpisodeDataSourceProvider(
        episodeRequest: EpisodeRequest
    ) : RemoteEpisodeDataSource = EpisodeRetrofitDataSource(episodeRequest)
}