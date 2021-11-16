package com.aniapi.videoplayer

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.aniapi.videoplayer.databinding.ActivityPlayerBinding
import com.dreamSubAnime.api.models.entities.Episode
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.properties.Delegates


@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class PlayerActivity : AppCompatActivity() {

    companion object {
        const val ANIME_ID = "anime_id"
        const val EPISODE_NUMBER = "episode_number"
        const val ANIME_TITLE = "anime_title"
        const val TAG = "PlayerActivity"
    }

    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityPlayerBinding.inflate(layoutInflater)
    }
    private var player: ExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L
    private var fullscreen: Boolean = false
    private lateinit var viewModel: PlayerViewModel
    private lateinit var uiState: StateFlow<EpisodeUiState>
    private lateinit var animeTitle: String
    private lateinit var titleView: AppCompatTextView
    private lateinit var backButton: AppCompatImageButton
    private var episodeNumber by Delegates.notNull<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        animeTitle = intent.extras?.getString(ANIME_TITLE)!!
        val animeId = intent.extras?.getLong(ANIME_ID)!!
        episodeNumber = intent.extras?.getInt(EPISODE_NUMBER)!!
        viewModel = ViewModelProvider(this,
            ViewModelFactory(animeId, episodeNumber)).get(PlayerViewModel::class.java)
        uiState = viewModel.uiState
        titleView = viewBinding.videoView.findViewById(R.id.header_title)
        backButton = viewBinding.videoView.findViewById(R.id.back_button)
    }

    private fun initializePlayer(episode: Episode) {
        val uri = episode.video

        player = ExoPlayer.Builder(this)
            .build()
            .also { exoPlayer ->
                viewBinding.videoView.player = exoPlayer
                val mediaItem = MediaItem.fromUri(uri)
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentWindow, playbackPosition)
                exoPlayer.prepare()
            }

    }

    private fun collectStateAndInitialize() {
        backButton.setOnClickListener {
            onBackPressed()
        }
        lifecycleScope.launch {
            uiState.collectLatest {
                when {
                    !it.loading && it.episodes != null && it.episodes.isNotEmpty() && it.currentlyPlaying >= 0 -> {
                        val currentEpisode = it.episodes[it.currentlyPlaying]
                        val title = if (currentEpisode.title != null && currentEpisode.title == "") {
                            animeTitle
                        } else {
                            currentEpisode.title
                        }
                        titleView.text = title
                        initializePlayer(currentEpisode)
                        Log.d(TAG + "episodes", it.episodes[it.currentlyPlaying].video)
                    }
                    it.failedLoading -> {
                        Toast.makeText(applicationContext, it.exception.toString(), Toast.LENGTH_SHORT)
                            .show()
                        onBackPressed()
                    }
                    it.currentlyPlaying < 0 -> {
                        Toast.makeText(applicationContext, "No episode to play", Toast.LENGTH_SHORT)
                            .show()
                        onBackPressed()
                    }
                    else -> {
                        Log.d(TAG, it.exception.toString())
                        Log.d(TAG + "episodes", it.episodes?.size.toString())
                    }
                }
            }
        }
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, viewBinding.videoView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun changeOrientation() {
        if (!fullscreen) {
//            WindowInsetsControllerCompat(window, viewBinding.videoView).let { controller ->
//                controller.show(WindowInsetsCompat.Type.systemBars())
//            }
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            fullscreen = true
        } else {
//            WindowCompat.setDecorFitsSystemWindows(window, false)
//            WindowInsetsControllerCompat(window, viewBinding.videoView).let { controller ->
//                controller.hide(WindowInsetsCompat.Type.systemBars())
//                controller.systemBarsBehavior =
//                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//            }
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            fullscreen = false
        }
    }

    private fun showSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window,
            viewBinding.videoView).show(WindowInsetsCompat.Type.systemBars())
    }

    private fun releasePlayer() {
        player?.run {
            playbackPosition = this.currentPosition
            currentWindow = this.currentMediaItemIndex
            playWhenReady = this.playWhenReady
            release()
        }
        player = null
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            collectStateAndInitialize()
        }
    }

    override fun onResume() {
        super.onResume()
        hideSystemUi()
        if (Util.SDK_INT < 24) {
            collectStateAndInitialize()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        playbackPosition = 0L
        currentWindow = 0
        playWhenReady = true
        fullscreen = false
    }
}