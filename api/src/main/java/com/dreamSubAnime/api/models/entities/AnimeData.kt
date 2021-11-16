package com.dreamSubAnime.api.models.entities


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnimeData(
    @Json(name = "count")
    val count: Int,
    @Json(name = "current_page")
    val currentPage: Int,
    @Json(name = "documents")
    val anime: List<Anime>,
    @Json(name = "last_page")
    val lastPage: Int,
    val message: String?
)