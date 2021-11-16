package com.dreamSubAnime.api.models.entities


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Source(
    @Json(name = "i18n")
    val i18n: String,
    @Json(name = "name")
    val name: String
)