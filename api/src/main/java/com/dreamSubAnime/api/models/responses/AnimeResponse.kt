package com.dreamSubAnime.api.models.responses

import com.dreamSubAnime.api.models.entities.AnimeData
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnimeResponse(
    @Json(name = "data")
    val animeData: AnimeData,
    @Json(name = "message")
    val message: String,
    @Json(name = "status_code")
    val statusCode: Int,
    @Json(name = "version")
    val version: String
)
