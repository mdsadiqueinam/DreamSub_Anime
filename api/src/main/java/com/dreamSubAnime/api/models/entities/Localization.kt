package com.dreamSubAnime.api.models.entities


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Localization(
    @Json(name = "i18n")
    val i18n: String,
    @Json(name = "label")
    val label: String
)