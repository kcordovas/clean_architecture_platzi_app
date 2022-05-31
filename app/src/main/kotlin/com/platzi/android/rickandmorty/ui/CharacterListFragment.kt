package com.platzi.android.rickandmorty.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.platzi.android.rickandmorty.R
import com.platzi.android.rickandmorty.adapters.CharacterGridAdapter
import com.platzi.android.rickandmorty.framework.requestmanager.APIConstants.BASE_API_URL
import com.platzi.android.rickandmorty.framework.requestmanager.CharacterRequest
import com.platzi.android.rickandmorty.framework.requestmanager.CharacterRetrofitDataSource
import com.platzi.android.rickandmorty.data.CharacterRepository
import com.platzi.android.rickandmorty.data.LocalCharacterDataSource
import com.platzi.android.rickandmorty.data.RemoteCharacterDataSource
import com.platzi.android.rickandmorty.framework.databasemanager.CharacterDatabase
import com.platzi.android.rickandmorty.framework.databasemanager.CharacterRoomDataSource
import com.platzi.android.rickandmorty.databinding.FragmentCharacterListBinding
import com.platzi.android.rickandmorty.domain.Character
import com.platzi.android.rickandmorty.presentation.CharacterListViewModel
import com.platzi.android.rickandmorty.presentation.CharacterListViewModel.CharacterListNavigation
import com.platzi.android.rickandmorty.presentation.CharacterListViewModel.CharacterListNavigation.*
import com.platzi.android.rickandmorty.presentation.Event
import com.platzi.android.rickandmorty.usescases.GetAllCharactersUseCase
import com.platzi.android.rickandmorty.utils.getViewModel
import com.platzi.android.rickandmorty.utils.setItemDecorationSpacing
import com.platzi.android.rickandmorty.utils.showLongToast
import kotlinx.android.synthetic.main.fragment_character_list.*


class CharacterListFragment : Fragment() {

    //region Fields

    private lateinit var characterGridAdapter: CharacterGridAdapter
    private lateinit var listener: OnCharacterListFragmentListener

    private val characterRequest: CharacterRequest by lazy {
        CharacterRequest(BASE_API_URL)
    }

    private val remoteCharacterDataSource : RemoteCharacterDataSource by lazy {
        CharacterRetrofitDataSource(characterRequest)
    }

    private val localCharacterDataSource: LocalCharacterDataSource by lazy {
        CharacterRoomDataSource(CharacterDatabase.getDatabase(activity!!.applicationContext))
    }

    private val characterRepository : CharacterRepository by lazy {
        CharacterRepository(remoteCharacterDataSource, localCharacterDataSource)
    }

    private val getAllCharactersUseCase: GetAllCharactersUseCase by lazy {
        GetAllCharactersUseCase(characterRepository)
    }

    private val characterListViewModel: CharacterListViewModel by lazy {
        getViewModel { CharacterListViewModel(getAllCharactersUseCase) }
    }

    private val onScrollListener: RecyclerView.OnScrollListener by lazy {
        object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val visibleItemCount: Int = layoutManager.childCount
                val totalItemCount: Int = layoutManager.itemCount
                val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()

                characterListViewModel.onLoadMoreItems(visibleItemCount, firstVisibleItemPosition, totalItemCount)
            }
        }
    }

    //endregion

    //region Override Methods & Callbacks

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            listener = context as OnCharacterListFragmentListener
        }catch (e: ClassCastException){
            throw ClassCastException("$context must implement OnCharacterListFragmentListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<FragmentCharacterListBinding>(
            inflater,
            R.layout.fragment_character_list,
            container,
            false
        ).apply {
            lifecycleOwner = this@CharacterListFragment
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        characterGridAdapter = CharacterGridAdapter { character ->
            listener.openCharacterDetail(character)
        }.also {
            setHasOptionsMenu(true)
        }

        rvCharacterList.run{
            addOnScrollListener(onScrollListener)
            setItemDecorationSpacing(resources.getDimension(R.dimen.list_item_padding))

            adapter = characterGridAdapter
        }

        srwCharacterList.setOnRefreshListener {
            characterListViewModel.onRetryGetAllCharacter(rvCharacterList.adapter?.itemCount ?: 0)
        }

        characterListViewModel.events.observe(this, Observer(this::validateEvents))

        characterListViewModel.onGetAllCharacters()
    }

    //endregion

    //region Private Methods

    private fun validateEvents(event: Event<CharacterListNavigation>?) {
        event?.getContentIfNotHandle()?.let { navigation ->
            when(navigation) {
                is ShowCharacterError -> navigation.run {
                    context?.showLongToast("Error -> ${error.message}")
                }
                is ShowCharacterList -> navigation.run {
                    characterGridAdapter.addData(characterList)
                }
                HideLoading -> {
                    srwCharacterList.isRefreshing = false
                }
                ShowLoading -> {
                    srwCharacterList.isRefreshing = true
                }
            }
        }
    }

    //endregion

    //region Inner Classes & Interfaces

    interface OnCharacterListFragmentListener {
        fun openCharacterDetail(character: Character)
    }

    //endregion

    //region Companion object

    companion object {

        fun newInstance(args: Bundle? = Bundle()) = CharacterListFragment().apply {
            arguments = args
        }
    }

    //endregion
}
