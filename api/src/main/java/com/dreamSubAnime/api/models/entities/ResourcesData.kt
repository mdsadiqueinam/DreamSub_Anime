package com.dreamSubAnime.api.models.entities


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResourcesData(
    @Json(name = "localizations")
    val localizations: List<Localization>?,
    @Json(name = "genres")
    val genres: List<String>?,
    @Json(name = "sources")
    val sources: List<Source>?
)