package com.dreamSubAnime.api.models.responses


import com.dreamSubAnime.api.models.entities.ResourcesData
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResourceResponse(
    @Json(name = "data")
    val resourcesData: ResourcesData?,
    @Json(name = "message")
    val message: String,
    @Json(name = "status_code")
    val statusCode: Int,
    @Json(name = "version")
    val version: String
)