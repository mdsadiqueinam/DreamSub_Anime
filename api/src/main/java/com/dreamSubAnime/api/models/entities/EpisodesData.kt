package com.dreamSubAnime.api.models.entities


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EpisodesData(
    @Json(name = "count")
    val count: Int,
    @Json(name = "current_page")
    val currentPage: Int,
    @Json(name = "documents")
    val episodes: List<Episode>,
    @Json(name = "last_page")
    val lastPage: Int,
    val message: String?
)