package com.aniapi.dreamsubanime

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@HiltAndroidApp
class DreamSubAnimeApplication : Application()