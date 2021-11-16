package com.dreamSubAnime.api.adapters

import com.dreamSubAnime.api.models.entities.EpisodesData
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader

class EpisodesAdapter {

    @FromJson
    fun fromJson(jsonReader: JsonReader, delegate: JsonAdapter<EpisodesData>): EpisodesData? {
        return if (jsonReader.peek() == JsonReader.Token.BEGIN_OBJECT) {
            delegate.fromJson(jsonReader)
        } else {
            EpisodesData(
                count = 0,
                currentPage = 1,
                episodes = listOf(),
                lastPage = 1,
                message = jsonReader.nextString()
            )
        }
    }
}