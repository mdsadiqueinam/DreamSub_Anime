package com.aniapi.dreamsubanime.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import com.dreamSubAnime.api.Injection
import com.dreamSubAnime.api.repositry.AnimeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @ExperimentalCoroutinesApi
    @ExperimentalPagingApi
    @Singleton
    @Provides
    fun provideAnimeRepository(@ApplicationContext context: Context): AnimeRepository {
        return Injection.provideAnimeRepository(context)
    }

}