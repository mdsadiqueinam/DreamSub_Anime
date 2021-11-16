package com.aniapi.videoplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import com.dreamSubAnime.api.Injection
import com.dreamSubAnime.api.models.entities.Episode
import com.dreamSubAnime.api.repositry.AnimeRepository
import com.dreamSubAnime.api.repositry.EpisodesResponseResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class PlayerViewModel(
    private val animeId: Long,
    private val episodeNumber: Int,
) : ViewModel() {

    private val repository: AnimeRepository = Injection.repository!!

    // UI state exposed to the UI
    private val _uiState = MutableStateFlow(EpisodeUiState(loading = true))
    val uiState: StateFlow<EpisodeUiState> = _uiState.asStateFlow()

    init {
        getEpisodes()
    }

    private fun getEpisodes() {
        viewModelScope.launch {
            val episodesResult =
                repository.getAnimeEpisodeByNumber(animeId = animeId, episodeNumber = episodeNumber)
            updateUi(episodesResult)
        }
    }

    private fun updateUi(episodesResult: EpisodesResponseResult) {
        _uiState.update { it ->
            when (episodesResult) {
                is EpisodesResponseResult.OnSuccess -> it.copy(
                    episodes = episodesResult.episodes,
                    loading = false,
                    currentlyPlaying = episodesResult.episodes.findIndex { episode ->
                        episode.source != "gogoanime"
                    }
                )
                is EpisodesResponseResult.OnError -> it.copy(
                    exception = episodesResult.exception,
                    loading = false
                )
            }
        }
    }
}

private fun <E> List<E>.findIndex(function: (E) -> Boolean): Int {
    if (this.isEmpty()) return -1
    for (i in 0..(this.size)) {
        if((function(this[i]))){
            return i
        }
    }
    return -1
}

data class EpisodeUiState(
    val loading: Boolean,
    val episodes: List<Episode>? = null,
    val currentlyPlaying: Int = 0,
    val exception: Exception? = null
) {
    /**
     * True if the episodes couldn't be found
     */
    val failedLoading: Boolean
        get() = !loading && episodes == null
}

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class ViewModelFactory(
    private val animeId: Long,
    private val episodeNumber: Int,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlayerViewModel(animeId, episodeNumber) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}