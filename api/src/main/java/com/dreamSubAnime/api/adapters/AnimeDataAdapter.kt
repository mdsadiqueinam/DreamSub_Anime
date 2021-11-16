package com.dreamSubAnime.api.adapters

import com.dreamSubAnime.api.models.entities.AnimeData
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader

class AnimeDataAdapter {

    @FromJson
    fun fromJson(jsonReader: JsonReader, delegate: JsonAdapter<AnimeData>): AnimeData? {
        return if (jsonReader.peek() == JsonReader.Token.BEGIN_OBJECT) {
            delegate.fromJson(jsonReader)
        } else {
            AnimeData(
                count = 0,
                currentPage = 1,
                anime = listOf(),
                lastPage = 1,
                message = jsonReader.nextString()
            )
        }
    }
}