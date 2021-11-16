package com.dreamSubAnime.api.services

import com.dreamSubAnime.api.models.responses.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AniApiService {

    @GET("anime")
    suspend fun fetchAnime(
        @Query("page") page: Int? = null,
        @Query("per_page") per_page: Int? = null,
        @Query("sort_fields") sort_fields: List<String>? = null,
        @Query("title") title: String? = null,
        @Query("anilist_id") anilist_id: Int? = null,
        @Query("mal_id") mal_id: Int? = null,
        @Query("formats") formats: List<Int>? = null,
        @Query("status") status: Int? = null,
        @Query("year") year: Int? = null,
        @Query("season") season: Int? = null,
        @Query("genres") genres: List<String>? = null,
        @Query("nsfw") nsfw: Boolean? = null,
    ): Response<AnimeResponse>

    @GET("anime/{id}")
    suspend fun fetchAnimeById(@Path("id") id: Long): Response<AnimeByIdResponse>

    @GET("random/anime/{count}")
    suspend fun fetchRandomAnime(@Path("count") count: Int): Response<RandomAnimeResponse>

    @GET("episode/{id}")
    suspend fun fetchEpisodeById(@Path("id") id: Int): Response<EpisodeResponse>

    @GET("episode")
    suspend fun fetchEpisodes(
        @Query("page") page: Int? = null,
        @Query("per_page") perPage: Int? = null,
        @Query("anime_id") animeId: Long? = null,
        @Query("number") episodeNumber: Int? = null,
        @Query("source") source: String? = null,
        @Query("locale") locale: String? = null
    ): Response<EpisodesResponse>

    @GET("resources/{version}/{type}")
    suspend fun fetchResources(
        @Path("version") version: Double,
        @Path("type") type: Int
    ): Response<ResourceResponse>
}