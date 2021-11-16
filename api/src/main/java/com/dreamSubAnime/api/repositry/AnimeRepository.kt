package com.dreamSubAnime.api.repositry

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dreamSubAnime.api.R
import com.dreamSubAnime.api.db.AnimeDatabase
import com.dreamSubAnime.api.models.entities.Anime
import com.dreamSubAnime.api.models.entities.Episode
import com.dreamSubAnime.api.services.AniApiClient
import com.dreamSubAnime.api.services.AnimeDao
import kotlinx.coroutines.flow.Flow
import java.io.IOException

@ExperimentalPagingApi
class AnimeRepository(private val client: AniApiClient, private val database: AnimeDatabase) {
    /**
     * Search repositories whose names match the query, exposed as a stream of data that will emit
     * every time we get more data from the network.
     */

    private val animeDao: AnimeDao = database.animeDao()

    fun getSearchedAnimeStream(query: String): Flow<PagingData<Anime>> {
        val trimmedQuery = query.trim()
        // appending '%' so we can allow other characters to be before and after the query string
        val dbQuery = "%${trimmedQuery.replace(' ', '%')}%"
        val pagingSourceFactory =  { animeDao.animeByName(dbQuery)}

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = AnimeSearchRemoteMediator(
                trimmedQuery,
                client,
                database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    suspend fun getAnimeById(id: Long): AnimeResponseResult {
        return try {
            val animeResult = animeDao.getAnimeById(id)
            AnimeResponseResult.OnSuccess(animeResult)
        } catch (e: Exception) {
            AnimeResponseResult.OnError(e)
        }
    }

    suspend fun refreshAnimeById(id: Long): AnimeResponseResult {
        try {
            val response = client.getAnimeById(id).body()
                ?: return AnimeResponseResult.OnError(IOException("Failed to receive response from api"))
            return when (response.statusCode) {
                200 -> {
                    animeDao.insert(response.anime)
                    AnimeResponseResult.OnSuccess(response.anime)
                }
                404 -> AnimeResponseResult.OnError(IllegalArgumentException("Invalid Id passed"))
                else -> AnimeResponseResult.OnError(IllegalAccessException(response.anime.message))
            }
        } catch (e: Exception) {
            return AnimeResponseResult.OnError(e)
        }
    }

    suspend fun getAnimeEpisodeByNumber(animeId: Long, episodeNumber: Int): EpisodesResponseResult {
        try {
            val response = client.getEpisodes(animeId = animeId, episodeNumber = episodeNumber).body()
                ?: return EpisodesResponseResult.OnError(IOException("Failed to receive response from api"))
            return when (response.statusCode) {
                200 -> EpisodesResponseResult.OnSuccess(response.episodesData.episodes)
                404 -> EpisodesResponseResult.OnError(IOException(response.message))
                else -> EpisodesResponseResult.OnError(IllegalAccessException(response.episodesData.message))
            }
        } catch (e: Exception) {
            return EpisodesResponseResult.OnError(e)
        }
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }
}

sealed class AnimeResponseResult {
    data class OnSuccess(val anime: Anime): AnimeResponseResult()
    data class OnError(val exception: Exception): AnimeResponseResult()
}

sealed class EpisodesResponseResult {
    data class OnSuccess(val episodes: List<Episode>) : EpisodesResponseResult()
    data class OnError(val exception: Exception) : EpisodesResponseResult()
}
