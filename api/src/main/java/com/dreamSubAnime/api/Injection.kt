package com.dreamSubAnime.api

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import com.dreamSubAnime.api.db.AnimeDatabase
import com.dreamSubAnime.api.repositry.AnimeRepository
import com.dreamSubAnime.api.services.AniApiClient
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
object Injection {

    var repository: AnimeRepository? = null

    fun provideAnimeRepository(context: Context): AnimeRepository {
        if (repository == null) {
            repository = AnimeRepository(AniApiClient.getClient(), AnimeDatabase.getInstance(context))
        }
        return repository!!
    }
}