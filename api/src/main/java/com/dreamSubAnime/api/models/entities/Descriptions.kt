package com.dreamSubAnime.api.models.entities


import androidx.room.ColumnInfo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Descriptions(
    @ColumnInfo(name = "english_description") @Json(name = "en") val english: String?,
    @ColumnInfo(name = "italian_description") @Json(name = "it") val italian: String?
)