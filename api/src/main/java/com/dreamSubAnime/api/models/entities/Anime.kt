package com.dreamSubAnime.api.models.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "anime")
@JsonClass(generateAdapter = true)
data class Anime(
    @PrimaryKey @Json(name = "id") val id: Long?,
    @ColumnInfo(name = "anilist_id") @Json(name = "anilist_id") val anilistId: Long?,
    @ColumnInfo(name = "banner_image") @Json(name = "banner_image") val bannerImage: String?,
    @ColumnInfo(name = "cover_color") @Json(name = "cover_color") val coverColor: String?,
    @ColumnInfo(name = "cover_image") @Json(name = "cover_image") val coverImage: String?,
    @ColumnInfo(name = "end_date") @Json(name = "end_date") val endDate: String?,
    @ColumnInfo(name = "episode_duration") @Json(name = "episode_duration") val episodeDuration: Int?,
    @ColumnInfo(name = "episodes_count") @Json(name = "episodes_count") val episodesCount: Int?,
    @ColumnInfo(name = "format") @Json(name = "format") val format: Format?,
    @ColumnInfo(name = "genres") @Json(name = "genres") val genres: List<String>?,
    @ColumnInfo(name = "mal_id") @Json(name = "mal_id") val malId: Long?,
    @ColumnInfo(name = "score") @Json(name = "score") val score: Int?,
    @ColumnInfo(name = "season_period") @Json(name = "season_period") val seasonPeriod: SeasonPeriod?,
    @ColumnInfo(name = "season_year") @Json(name = "season_year") val seasonYear: Int?,
    @ColumnInfo(name = "start_date") @Json(name = "start_date") val startDate: String?,
    @ColumnInfo(name = "status") @Json(name = "status") val status: Status?,
    @Embedded @Json(name = "descriptions") val descriptions: Descriptions?,
    @Embedded @Json(name = "titles") val titles: Titles?,
    val message: String?
)


//@JsonClass(generateAdapter = true)
//data class Anime(
//    @Json(name = "anilist_id")
//    val anilistId: Int?,
//    @Json(name = "banner_image")
//    val bannerImage: String?,
//    @Json(name = "cover_color")
//    val coverColor: String?,
//    @Json(name = "cover_image")
//    val coverImage: String?,
//    @Json(name = "descriptions")
//    val descriptions: Descriptions?,
//    @Json(name = "end_date")
//    val endDate: String?,
//    @Json(name = "episode_duration")
//    val episodeDuration: Int?,
//    @Json(name = "episodes_count")
//    val episodesCount: Int?,
//    @Json(name = "format")
//    val format: Format?,
//    @Json(name = "genres")
//    val genres: List<String?>?,
//    @Json(name = "id")
//    val id: Int?,
//    @Json(name = "mal_id")
//    val malId: Int?,
//    @Json(name = "score")
//    val score: Int?,
//    @Json(name = "season_period")
//    val seasonPeriod: SeasonPeriod?,
//    @Json(name = "season_year")
//    val seasonYear: Int?,
//    @Json(name = "start_date")
//    val startDate: String?,
//    @Json(name = "status")
//    val status: Status?,
//    @Json(name = "titles")
//    val titles: Titles?,
//    val message: String?
//)
