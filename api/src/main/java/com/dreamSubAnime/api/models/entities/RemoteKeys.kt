package com.dreamSubAnime.api.models.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    val animeId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)
