package com.dreamSubAnime.api.models.entities

enum class Format(val value: Int) {
    TV(0),
    TV_SHORT(1),
    MOVIE(2),
    SPECIAL(3),
    OVA(4),
    ONA(5),
    MUSIC(6),
    UNKNOWN(-1);


    companion object {
        fun fromInt(type: Int) = values().associateBy(Format::value)[type] ?: UNKNOWN
    }
}