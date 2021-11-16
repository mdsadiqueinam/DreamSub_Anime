package com.dreamSubAnime.api.models.entities


enum class SeasonPeriod(val value: Int) {
    WINTER(0),
    SPRING(1),
    SUMMER(2),
    FALL(3),
    UNKNOWN(4);

    companion object {
        fun fromInt(type: Int) = values().associateBy(SeasonPeriod::value)[type] ?: UNKNOWN
    }
}