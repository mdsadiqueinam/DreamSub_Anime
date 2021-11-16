package com.dreamSubAnime.api.adapters

import com.dreamSubAnime.api.models.entities.Anime
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader

class AnimeAdapter {

    @FromJson
    fun fromJson(jsonReader: JsonReader, delegate: JsonAdapter<Anime>): Anime? {
        return if (jsonReader.peek() == JsonReader.Token.BEGIN_OBJECT) {
            delegate.fromJson(jsonReader)
        } else {
            Anime(
                anilistId = null,
                bannerImage = null,
                coverColor = null,
                coverImage = null,
                descriptions = null,
                endDate = null,
                episodeDuration = null,
                episodesCount = null,
                format = null,
                genres = null,
                id = null,
                malId = null,
                score = null,
                seasonPeriod = null,
                seasonYear = null,
                startDate = null,
                status = null,
                titles = null,
                message = jsonReader.nextString()
            )
        }
    }

}