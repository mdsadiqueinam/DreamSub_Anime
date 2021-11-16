package com.dreamSubAnime.api.converters

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromListToString(value: List<String>?) : String {
        if (value == null || value.isEmpty()) return ""
        return value.toString()
    }

    @TypeConverter
    fun fromStringToList(value: String?) : List<String> {
        if (value == null || value == "") return listOf()
        return value.substring(1, value.length - 1).split(", ")
    }
}