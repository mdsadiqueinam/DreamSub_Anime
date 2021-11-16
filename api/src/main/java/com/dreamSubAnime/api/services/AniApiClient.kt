package com.dreamSubAnime.api.services

import com.dreamSubAnime.api.models.entities.Format
import com.dreamSubAnime.api.models.entities.SeasonPeriod
import com.dreamSubAnime.api.models.entities.Status
import com.dreamSubAnime.api.models.entities.Type
import com.dreamSubAnime.api.adapters.*
import com.dreamSubAnime.api.models.responses.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okio.IOException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class AniApiClient {

    companion object {
        private const val BASE_URL = "https://api.aniapi.com/v1/"

        private val moshi = Moshi.Builder()
            .add(AnimeDataAdapter())
            .add(AnimeAdapter())
            .add(EpisodesAdapter())
            .add(KotlinJsonAdapterFactory())
            .add(FormatAdapter())
            .add(SeasonPeriodAdapter())
            .add(StatusAdapter())
            .build()

        private val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .build()

        private val API: AniApiService by lazy {
            retrofit.create(AniApiService::class.java)
        }

        @Volatile
        private var INSTANCE: AniApiClient? = null

        fun getClient() = INSTANCE ?: synchronized(this) {
            INSTANCE ?: AniApiClient().also {
                INSTANCE = it
            }
        }
    }

    suspend fun getAnime(
        page: Int? = null,
        perPage: Int? = null,
        sortFields: List<String> = listOf("id"),
        title: String? = null,
        anilistId: Int? = null,
        malId: Int? = null,
        year: Int? = null,
        genres: List<String>? = null,
        nsfw: Boolean? = null,
    ): Response<AnimeResponse> =
        API.fetchAnime(
            page = page,
            per_page = perPage,
            sort_fields = sortFields,
            title = title,
            anilist_id = anilistId,
            mal_id = malId,
            year = year,
            genres = genres,
            nsfw = nsfw
        )

    suspend fun getAnimeByFormats(formats: List<Format>): Response<AnimeResponse> {
        val newFormats = formats.map { it.value }
        return API.fetchAnime(formats = newFormats)
    }

    suspend fun getAnimeBySeason(season: SeasonPeriod): Response<AnimeResponse> =
        API.fetchAnime(season = season.value)

    suspend fun getAnimeByStatus(status: Status): Response<AnimeResponse> =
        API.fetchAnime(status = status.value)

    suspend fun getAnimeById(id: Long): Response<AnimeByIdResponse> {
        if (id < 1) throw IOException("Anime ID cannot be less than 1")
        return API.fetchAnimeById(id)
    }

    suspend fun getRandomAnime(count: Int = 1): Response<RandomAnimeResponse> {
        if (count < 1 || count > 50) throw IOException("count cannot be less than 1 and greater than 50")
        return API.fetchRandomAnime(count)
    }

    suspend fun getEpisodeById(id: Int): Response<EpisodeResponse> {
        if (id < 1) throw IOException("Episode ID cannot be less than 1")
        return API.fetchEpisodeById(id)
    }

    suspend fun getEpisodes(
        page: Int = 1,
        perPage: Int = 10,
        animeId: Long? = null,
        episodeNumber: Int? = null,
        source: String? = null,
        locale: String = "it"
    ): Response<EpisodesResponse> {
        if (page < 1 || perPage < 1 || (animeId != null && animeId < 1) || (episodeNumber != null && episodeNumber < 1))
            throw IOException("Page starts from 1, per_page should be greater than 0, anime_id and number must be greater than 0")
        return API.fetchEpisodes(
            page = page,
            perPage = perPage,
            animeId = animeId,
            episodeNumber = episodeNumber,
            source = source,
            locale = locale
        )
    }

    suspend fun getResource(type: Type): Response<ResourceResponse> =
        API.fetchResources(1.0, type.value)
}