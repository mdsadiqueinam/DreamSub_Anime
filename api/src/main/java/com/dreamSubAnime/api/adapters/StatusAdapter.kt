package com.dreamSubAnime.api.adapters

import com.dreamSubAnime.api.models.entities.Status
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class StatusAdapter {
    @ToJson
    fun toJson(type: Status): Int = type.value

    @FromJson
    fun fromJson(value: Int): Status = Status.fromInt(value)
}