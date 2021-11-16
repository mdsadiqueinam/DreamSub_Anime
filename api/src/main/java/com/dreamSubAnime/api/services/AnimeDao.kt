package com.dreamSubAnime.api.services

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dreamSubAnime.api.models.entities.Anime
import com.dreamSubAnime.api.models.entities.Result

@Dao
interface AnimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(anime: List<Anime>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(anime: Anime)

    @Query("DELETE FROM anime")
    suspend fun clearAnime()

    @Query("SELECT * FROM anime")
    fun getAnime(): PagingSource<Int, Anime>

    @Query("SELECT * FROM anime WHERE english_title LIKE :queryString or japanese_title LIKE :queryString")
    fun animeByName(queryString: String): PagingSource<Int, Anime>

    @Query("SELECT * FROM anime WHERE id = :id")
    suspend fun getAnimeById(id: Long): Anime
}