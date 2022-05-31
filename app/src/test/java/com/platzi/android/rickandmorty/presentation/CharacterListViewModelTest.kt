package com.platzi.android.rickandmorty.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import com.platzi.android.rickandmorty.domain.Character
import com.platzi.android.rickandmorty.domain.Location
import com.platzi.android.rickandmorty.domain.Origin
import com.platzi.android.rickandmorty.presentation.CharacterListViewModel.CharacterListNavigation
import com.platzi.android.rickandmorty.usescases.GetAllCharactersUseCase
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CharacterListViewModelTest {

    @get:Rule
    val rxSchedulerRules = RxSchedulerRules()

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var getAllCharacterUseCase: GetAllCharactersUseCase

    @Mock
    lateinit var eventObserver: Observer<Event<CharacterListNavigation>>

    @Mock
    private lateinit var characterListViewModel: CharacterListViewModel

    @Before
    fun setup() {
        characterListViewModel = CharacterListViewModel(getAllCharacterUseCase)
    }

    @Test
    fun `onGetAllCharacters should return an expected success list of characters`() {
        val excpectedResult = listOf(mockedCharacter.copy(id = 1))
        given(getAllCharacterUseCase.invoke(any())).willReturn(Single.just(excpectedResult))

        characterListViewModel.events.observeForever(eventObserver)
        characterListViewModel.onGetAllCharacters()
        verify(eventObserver).onChanged(Event(
            CharacterListNavigation.ShowCharacterList(
                excpectedResult
            )
        ))
    }
}

val mockedOrigin = Origin(
    "",
    ""
)

val mockedLocation= Location(
    "",
    ""
)
val mockedCharacter = Character(
    0,
    "",
    null,
    "",
    "",
    "",
    mockedOrigin,
    mockedLocation,
    listOf("")
)