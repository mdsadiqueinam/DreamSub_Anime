package com.aniapi.dreamsubanime.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.ExperimentalPagingApi
import coil.compose.rememberImagePainter
import com.aniapi.dreamsubanime.R
import com.aniapi.dreamsubanime.ui.theme.DreamSubAnimeTheme
import com.aniapi.dreamsubanime.viewModels.AnimeViewModel
import com.dreamSubAnime.api.models.entities.*
import com.google.accompanist.insets.systemBarsPadding
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@ExperimentalPagingApi
@Composable
fun AnimeScreen(
    viewModel: AnimeViewModel,
    onBack: () -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    startPlayer: (animeId: Long, episodeNumber: Int, animeTitle: String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val anime = uiState.anime
    val loading = uiState.loading
    val scrollState = rememberScrollState()

    val title = when {
        loading -> {
            "Loading..."
        }
        anime == null -> {
            "Something Went Wrong"
        }
        else -> {
            anime.titles?.english
        }
    }



    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(
                hostState = it,
                modifier = Modifier.systemBarsPadding()
            )
        },
        topBar = {
            AnimeScreenTopBar(
                title = title ?: "No Title",
                onBack = onBack
            )
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = uiState.loading),
            onRefresh = { viewModel.refresh() }
        ) {
            LoadingEmptyOrContent(
                loading = loading,
                anime = uiState.anime,
                scaffoldState = scaffoldState,
                scrollState = scrollState,
                modifier = modifier,
                startPlayer = startPlayer
            )
        }
    }

    LaunchedEffect(uiState) {
        if (uiState.failedLoading) {
            scaffoldState.snackbarHostState.showSnackbar(
                message = uiState.exception.toString()
            )
            onBack()
        }
    }

}


@Composable
private fun LoadingEmptyOrContent(
    loading: Boolean,
    anime: Anime?,
    scaffoldState: ScaffoldState,
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
    startPlayer: (animeId: Long, episodeNumber: Int, animeTitle: String) -> Unit
) {
    when {
        loading -> {
            FullScreenLoading()
        }
        anime == null -> {
            FullEmptyScreen(message = "Something Went Wrong")
        }
        else -> {
            AnimeScreen(
                anime = anime,
                scaffoldState,
                scrollState,
                modifier,
                startPlayer
            )
        }
    }
}

@Composable
fun AnimeScreen(
    anime: Anime,
    scaffoldState: ScaffoldState,
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
    startPlayer: (animeId: Long, episodeNumber: Int, animeTitle: String) -> Unit
) {
    val title = anime.titles?.english ?: stringResource(R.string.no_title)
    val image = anime.bannerImage ?: anime.coverImage
    val painter = rememberImagePainter(
        data = image,
        onExecute = { _, _ -> true },
        builder = {
            crossfade(true)
            placeholder(R.drawable.ic_broken_image)
        }
    )
    val descriptions: String = if (anime.descriptions?.english == null || anime.descriptions?.english == ""){
        stringResource(R.string.no_description)
    } else {
        val des = anime.descriptions!!.english!!
        des.trim('<')
    }
    val genres: String = if (anime.genres?.isNotEmpty() == true) {
        anime.genres.toString().trim('[',']')
    } else {
        ""
    }

    Column(Modifier.verticalScroll(scrollState)) {
        Box(
            modifier = Modifier.height(200.dp)
        ) {
            Image(
                painter = painter,
                contentDescription = "Cover Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Column(
            modifier = modifier.padding(all = 23.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = title,
                letterSpacing = (1.5).sp,
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append(stringResource(R.string.description))
                    }
                    append(descriptions)
                }
            )
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append(stringResource(R.string.genres))
                    }
                    append(genres)
                }
            )
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append(stringResource(R.string.season_year))
                    }
                    append(anime.seasonYear?.toString() ?: "")
                }
            )
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append(stringResource(R.string.status))
                    }
                    append(anime.status?.name ?: "")
                }
            )
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                        append(stringResource(R.string.total_episodes))
                    }
                    append(anime.episodesCount?.toString() ?: "0")
                }
            )
            Button(onClick = { startPlayer(anime.id!!, 1, title) }) {
                Text(text = "Episode Number 1")
            }
        }
    }
}

@Suppress("SameParameterValue")
@Composable
private fun FullEmptyScreen(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.h5
        )
    }
}

@Preview
@Composable
fun AnimeScreenPreview() {
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()
    val anime = Anime(
        id = 11,
        anilistId = 12,
        bannerImage = "https://www.cheatsheet.com/wp-content/uploads/2021/09/Demon-Slayer-Season-2-1200x901.jpeg",
        coverColor = "Red",
        coverImage = "https://www.cheatsheet.com/wp-content/uploads/2021/09/Demon-Slayer-Season-2-1200x901.jpeg",
        endDate = "12/12/2024",
        episodeDuration = 24,
        episodesCount = 18,
        format = Format.TV,
        genres = listOf("action","drama", "demon"),
        malId = 25,
        score = 200,
        seasonPeriod = SeasonPeriod.WINTER,
        seasonYear = 2020,
        startDate = null,
        status = Status.RELEASING,
        descriptions = Descriptions(english = "In 1973, the invasion of an extra terrestrial life form, the BETA, began a war that has driven mankind to the brink of extinction. In an attempt to counter the BETA's overwhelming strength in numbers, mankind has developed the humanoid weapons known as TSFs, deploying them on the front lines of their Anti-BETA War all across the globe. However, mankind still lost the majority of Eurasia to the superior numbers of the marching BETA. For nearly 30 years, mankind has remained bogged down in its struggle against the BETA with no hope in sight.", italian = null),
        titles = Titles(english = "Demon Slayer", null, null),
        message = null
    )
    DreamSubAnimeTheme {
        AnimeScreen(anime = anime,
            scaffoldState = scaffoldState,
            scrollState = scrollState
        ) { _, _, _ ->

        }
    }
}