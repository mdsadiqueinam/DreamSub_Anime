package com.dreamSubAnime.api.adapters

import com.dreamSubAnime.api.models.entities.Format
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class FormatAdapter {
    @ToJson
    fun toJson(type: Format): Int =  type.value

    @FromJson
    fun fromJson(value: Int): Format = Format.fromInt(value)

}