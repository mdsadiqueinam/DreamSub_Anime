package com.dreamSubAnime.api.repositry

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.dreamSubAnime.api.db.AnimeDatabase
import com.dreamSubAnime.api.models.entities.Anime
import com.dreamSubAnime.api.models.entities.RemoteKeys
import com.dreamSubAnime.api.models.responses.AnimeResponse
import retrofit2.HttpException
import java.io.IOException

private const val ANIME_STARTING_PAGE_INDEX = 1

@ExperimentalPagingApi
abstract class AnimeRemoteMediator(
    private val animeDatabase: AnimeDatabase
) : RemoteMediator<Int, Anime>() {

    override suspend fun initialize(): InitializeAction {
        // Launch remote refresh as soon as paging starts and do not trigger remote prepend or
        // append until refresh has succeeded. In cases where we don't mind showing out-of-date,
        // cached offline data, we can return SKIP_INITIAL_REFRESH instead to prevent paging
        // triggering remote refresh.
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Anime>): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: ANIME_STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
//            {
//                val remoteKeys = getRemoteKeyForFirstItem(state)
//                // If remoteKeys is null, that means the refresh result is not in the database yet.
//                // We can return Success with `endOfPaginationReached = false` because Paging
//                // will call this method again if RemoteKeys becomes non-null.
//                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
//                // the end of pagination for prepend.
//                val prevKey = remoteKeys?.prevKey
//                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
//                prevKey
//            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with `endOfPaginationReached = false` because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its prevKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }


        try {
            val apiResponse = getResponse(page = page, perPage = state.config.pageSize)

            val anime = apiResponse.animeData.anime
            val endOfPaginationReached = anime.isEmpty()
            animeDatabase.withTransaction {
                // clear all tables in the database
                if (loadType == LoadType.REFRESH) {
                    animeDatabase.remoteKeysDao().clearRemoteKeys()
                    animeDatabase.animeDao().clearAnime()
                }
                val currentPage = apiResponse.animeData.currentPage
                val lastPage = apiResponse.animeData.lastPage
                val prevKey = if (currentPage != null && currentPage != ANIME_STARTING_PAGE_INDEX) {
                    currentPage - 1
                } else {
                    null
                }
                val nextKey = if (currentPage != null && currentPage != lastPage) {
                    currentPage + 1
                } else {
                    null
                }
                val keys = anime.map {
                    RemoteKeys(animeId = it.id!!, prevKey = prevKey, nextKey = nextKey)
                }
                keys.let { animeDatabase.remoteKeysDao().insertAll(keys) }
                anime.let { animeDatabase.animeDao().insertAll(anime) }
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Anime>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { anime ->
                // Get the remote keys of the first items retrieved
                anime.id?.let { animeDatabase.remoteKeysDao().remoteKeysAnimeId(it) }
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Anime>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { anime ->
                // Get the remote keys of the first items retrieved
                anime.id?.let { animeDatabase.remoteKeysDao().remoteKeysAnimeId(it) }
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Anime>
    ): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { animeId ->
                animeDatabase.remoteKeysDao().remoteKeysAnimeId(animeId)
            }
        }
    }

    abstract suspend fun getResponse(page: Int, perPage: Int): AnimeResponse
}