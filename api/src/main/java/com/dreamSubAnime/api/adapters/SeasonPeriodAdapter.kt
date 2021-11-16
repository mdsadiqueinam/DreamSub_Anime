package com.dreamSubAnime.api.adapters

import com.dreamSubAnime.api.models.entities.SeasonPeriod
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class SeasonPeriodAdapter {
    @ToJson
    fun toJson(type: SeasonPeriod): Int = type.value

    @FromJson
    fun fromJson(value: Int): SeasonPeriod = SeasonPeriod.fromInt(value)
}