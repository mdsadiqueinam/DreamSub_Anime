package com.dreamSubAnime.api.models.entities


import androidx.room.ColumnInfo
import androidx.room.Entity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Titles(
    @ColumnInfo(name = "english_title") @Json(name = "en") val english: String?,
    @ColumnInfo(name = "italian_title") @Json(name = "it") val italian: String?,
    @ColumnInfo(name = "japanese_title") @Json(name = "jp") val japanese: String?
)