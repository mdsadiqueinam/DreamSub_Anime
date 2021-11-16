package com.dreamSubAnime.api.repositry

import androidx.paging.ExperimentalPagingApi
import com.dreamSubAnime.api.services.AniApiClient
import com.dreamSubAnime.api.db.AnimeDatabase
import com.dreamSubAnime.api.models.responses.AnimeResponse

@ExperimentalPagingApi
class AnimeSearchRemoteMediator(private val query: String, private val client: AniApiClient, database: AnimeDatabase) :
    AnimeRemoteMediator(database) {

    override suspend fun getResponse(page: Int, perPage: Int): AnimeResponse {
        return client.getAnime(page = page, perPage = perPage, title = query).body()!!
    }

}
