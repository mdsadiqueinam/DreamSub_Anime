package com.aniapi.dreamsubanime

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.paging.ExperimentalPagingApi
import coil.annotation.ExperimentalCoilApi
import com.aniapi.dreamsubanime.ui.DreamSubAnimeApp
import com.aniapi.videoplayer.PlayerActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoilApi
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            DreamSubAnimeApp(
                startPlayer = {animeId, episodeNumber, animeTitle ->
                    startPlayer(animeId, episodeNumber, animeTitle)
                }
            )
        }
    }

    private fun startPlayer(animeId: Long, episodeNumber: Int, animeTitle: String) {
        val intent = Intent(this@MainActivity, PlayerActivity::class.java).apply {
            putExtra(PlayerActivity.ANIME_ID, animeId)
            putExtra(PlayerActivity.EPISODE_NUMBER, episodeNumber)
            putExtra(PlayerActivity.ANIME_TITLE, animeTitle)
        }
        startActivity(intent)
    }
}
