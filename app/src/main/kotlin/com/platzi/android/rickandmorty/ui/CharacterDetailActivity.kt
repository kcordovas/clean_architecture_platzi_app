package com.platzi.android.rickandmorty.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.platzi.android.rickandmorty.R
import com.platzi.android.rickandmorty.adapters.EpisodeListAdapter
import com.platzi.android.rickandmorty.api.APIConstants.BASE_API_URL
import com.platzi.android.rickandmorty.api.CharacterServer
import com.platzi.android.rickandmorty.api.EpisodeRequest
import com.platzi.android.rickandmorty.database.CharacterDao
import com.platzi.android.rickandmorty.database.CharacterDatabase
import com.platzi.android.rickandmorty.databinding.ActivityCharacterDetailBinding
import com.platzi.android.rickandmorty.presentation.CharacterDetailViewModel
import com.platzi.android.rickandmorty.usescases.GetEpisodeFromCharacterUseCase
import com.platzi.android.rickandmorty.usescases.GetFavoriteCharacterStatusCase
import com.platzi.android.rickandmorty.usescases.UpdateFavoriteCharacterStatusUseCase
import com.platzi.android.rickandmorty.utils.Constants
import com.platzi.android.rickandmorty.utils.bindCircularImageUrl
import com.platzi.android.rickandmorty.utils.getViewModel
import com.platzi.android.rickandmorty.utils.showLongToast
import kotlinx.android.synthetic.main.activity_character_detail.*

class CharacterDetailActivity : AppCompatActivity() {

    //region Fields
    private lateinit var episodeListAdapter: EpisodeListAdapter
    private lateinit var binding: ActivityCharacterDetailBinding
    private val episodeRequest: EpisodeRequest by lazy {
        EpisodeRequest(BASE_API_URL)
    }
    private val characterDao: CharacterDao by lazy {
        CharacterDatabase.getDatabase(application).characterDao()
    }

    private val getEpisodeFromCharacterUseCase : GetEpisodeFromCharacterUseCase by lazy {
        GetEpisodeFromCharacterUseCase(episodeRequest)
    }

    private val getFavoriteCharacterStatusCase : GetFavoriteCharacterStatusCase by lazy {
        GetFavoriteCharacterStatusCase(characterDao)
    }

    private val updateFavoriteCharacterStatusUseCase : UpdateFavoriteCharacterStatusUseCase by lazy {
        UpdateFavoriteCharacterStatusUseCase(characterDao)
    }

    private val viewModel: CharacterDetailViewModel by lazy {
        getViewModel {
            CharacterDetailViewModel(
                getEpisodeFromCharacterUseCase,
                intent.getParcelableExtra(Constants.EXTRA_CHARACTER),
                getFavoriteCharacterStatusCase,
                updateFavoriteCharacterStatusUseCase
            )
        }
    }

    //endregion

    //region Override Methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_character_detail)
        binding.lifecycleOwner = this@CharacterDetailActivity

        episodeListAdapter = EpisodeListAdapter { episode ->
            this@CharacterDetailActivity.showLongToast("Episode -> $episode")
        }
        rvEpisodeList.adapter = episodeListAdapter

        viewModel.characterValues.observe(this, Observer(this::loadCharacter))
        viewModel.isFavoriteCharacter.observe(this, Observer(this::updateFavoriteIcon))
        viewModel.events.observe(this, Observer { event ->
            event?.getContentIfNotHandle()?.let { navigation ->
                when (navigation) {
                    CharacterDetailViewModel.CharacterDetailsNavigation.CloseActivity -> {
                        this@CharacterDetailActivity.showLongToast(R.string.error_no_character_data)
                        finish()
                    }
                    is CharacterDetailViewModel.CharacterDetailsNavigation.ShowCharacterDetailsError -> navigation.run {
                        this@CharacterDetailActivity.showLongToast("Error -> ${throwable.message}")
                    }
                    is CharacterDetailViewModel.CharacterDetailsNavigation.ShowEpisodesServerList -> navigation.run {
                        episodeListAdapter.updateData(listEpisodeServer)
                    }
                    CharacterDetailViewModel.CharacterDetailsNavigation.ShowLoading -> {
                        episodeProgressBar.isVisible = true
                    }
                    CharacterDetailViewModel.CharacterDetailsNavigation.HideLoading -> {
                        episodeProgressBar.isVisible = false
                    }
                }
            }
        })

        viewModel.onCharacterValidation()
        characterFavorite.setOnClickListener { viewModel.onUpdateFavoriteCharacterStatus() }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    //endregion

    //region Private Methods

    private fun loadCharacter(character: CharacterServer?) {
        binding.characterImage.bindCircularImageUrl(
            url = character!!.image,
            placeholder = R.drawable.ic_camera_alt_black,
            errorPlaceholder = R.drawable.ic_broken_image_black
        )
        binding.characterDataName = character.name
        binding.characterDataStatus = character.status
        binding.characterDataSpecies = character.species
        binding.characterDataGender = character.gender
        binding.characterDataOriginName = character.origin.name
        binding.characterDataLocationName = character.location.name
    }

    private fun updateFavoriteIcon(isFavorite: Boolean?) {
        characterFavorite.setImageResource(
            if (isFavorite != null && isFavorite) {
                R.drawable.ic_favorite
            } else {
                R.drawable.ic_favorite_border
            }
        )
    }

    //endregion
}
