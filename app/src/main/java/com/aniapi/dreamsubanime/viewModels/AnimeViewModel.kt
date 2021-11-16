package com.aniapi.dreamsubanime.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import com.dreamSubAnime.api.models.entities.Anime
import com.dreamSubAnime.api.repositry.AnimeRepository
import com.dreamSubAnime.api.repositry.AnimeResponseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

/**
 * UI state for the Anime screen
 */
data class AnimeUiState(
    val anime: Anime? = null,
    val loading: Boolean = false,
    val exception: Exception? = null
) {
    /**
     * True if the anime couldn't be found
     */
    val failedLoading: Boolean
        get() = !loading && anime == null
}

@ExperimentalPagingApi
@HiltViewModel
class AnimeViewModel @Inject constructor(
    private val repository: AnimeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val animeId: Long = savedStateHandle.get<Long>(ANIME_ID_KEY)!!

    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(AnimeUiState(loading = true))
    val uiState: StateFlow<AnimeUiState> = _uiState.asStateFlow()

    init {
        getAnimeFromDatabase()
    }

    private fun getAnimeFromDatabase() {
        viewModelScope.launch {
            val animeResult = repository.getAnimeById(animeId)
            updateUi(animeResult)
        }
    }

    private fun updateUi(animeResult: AnimeResponseResult) {
        _uiState.update {
            when (animeResult) {
                is AnimeResponseResult.OnSuccess -> it.copy(
                    anime = animeResult.anime,
                    loading = false
                )
                is AnimeResponseResult.OnError -> it.copy(
                    loading = false,
                    exception = animeResult.exception
                )
            }
        }
    }

    fun refresh() {
        _uiState.update {
            it.copy(loading = true)
        }
        viewModelScope.launch {
            val animeResult = repository.refreshAnimeById(id = animeId)
            updateUi(animeResult)
        }
    }

    companion object {
        const val ANIME_ID_KEY = "animeId"
    }

}