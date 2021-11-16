package com.dreamSubAnime.api.models.entities


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Episode(
    @Json(name = "anime_id")
    val animeId: Int,
    @Json(name = "id")
    val id: Int,
    @Json(name = "locale")
    val locale: String?,
    @Json(name = "number")
    val number: Int,
    @Json(name = "source")
    val source: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "video")
    val video: String
)