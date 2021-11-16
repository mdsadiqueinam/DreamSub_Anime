package com.dreamSubAnime.api.db

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dreamSubAnime.api.converters.Converters
import com.dreamSubAnime.api.models.entities.Anime
import com.dreamSubAnime.api.models.entities.RemoteKeys
import com.dreamSubAnime.api.services.AnimeDao
import com.dreamSubAnime.api.services.RemoteKeysDao

@Database(
    entities = [Anime::class,  RemoteKeys::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class AnimeDatabase : RoomDatabase() {

    abstract fun animeDao(): AnimeDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {

        private val MIGRATION_2_1 = object : Migration(2, 1) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE anime RENAME COLUMN anime_id TO id")
            }
        }

        @Volatile
        private var INSTANCE: AnimeDatabase? = null

        fun getInstance(context: Context): AnimeDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                AnimeDatabase::class.java, "Anime.db")
                .addMigrations(MIGRATION_2_1)
                .build()
    }
}