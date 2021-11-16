package com.dreamSubAnime.api.models.entities

enum class Status(val value: Int) {
    FINISHED(0),
    RELEASING(1),
    NOT_YET_RELEASED(2),
    CANCELLED(3),
    UNKNOWN(-1);

    companion object {
        fun fromInt(type: Int) = values().associateBy(Status::value)[type] ?: UNKNOWN
    }
}