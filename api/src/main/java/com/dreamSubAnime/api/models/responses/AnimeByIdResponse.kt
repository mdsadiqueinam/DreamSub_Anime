package com.dreamSubAnime.api.models.responses

import com.dreamSubAnime.api.models.entities.Anime
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AnimeByIdResponse(
    @Json(name = "data")
    val anime: Anime,
    @Json(name = "message")
    val message: String,
    @Json(name = "status_code")
    val statusCode: Int,
    @Json(name = "version")
    val version: String
)
