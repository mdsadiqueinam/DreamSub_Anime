package com.dreamSubAnime.api.models.responses


import com.dreamSubAnime.api.models.entities.EpisodesData
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EpisodesResponse(
    @Json(name = "data")
    val episodesData: EpisodesData,
    @Json(name = "message")
    val message: String,
    @Json(name = "status_code")
    val statusCode: Int,
    @Json(name = "version")
    val version: String
)