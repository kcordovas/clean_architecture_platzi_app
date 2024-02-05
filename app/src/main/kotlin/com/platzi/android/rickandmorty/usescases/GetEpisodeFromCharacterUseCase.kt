package com.platzi.android.rickandmorty.usescases

import com.platzi.android.rickandmorty.data.EpisodeRepository

class GetEpisodeFromCharacterUseCase(
    private val episodeRepository: EpisodeRepository
) {

    fun invoke(episodeUrlList: List<String>) = episodeRepository.getEpisodeCharacter(episodeUrlList)
}