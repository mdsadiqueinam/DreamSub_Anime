package com.aniapi.dreamsubanime.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.annotation.ExperimentalCoilApi
import com.aniapi.dreamsubanime.R
import com.aniapi.dreamsubanime.viewModels.SearchAnimeViewModel
import com.aniapi.dreamsubanime.viewModels.UiAction
import com.aniapi.dreamsubanime.viewModels.UiState
import com.dreamSubAnime.api.models.entities.Anime
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.systemBarsPadding
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

/**
 * Displays the Home screen.
 *
 * Note: AAC ViewModels don't work with Compose Previews currently.
 *
 * @param viewModel ViewModel that handles the business logic of this screen
 * @param navigateToAnime (event) request navigation to Anime details screen
 * @param openDrawer (event) request opening the app drawer
 * @param scaffoldState (state) state for the [Scaffold] component on this screen
 */

@ExperimentalCoilApi
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@Composable
fun HomeScreen(
    viewModel: SearchAnimeViewModel,
    navigateToAnime: (Long) -> Unit,
    openDrawer: () -> Unit,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {

    HomeScreen(
        uiAction = viewModel.accept,
        uiState = viewModel.state,
        navigateToAnime = navigateToAnime,
        openDrawer = openDrawer,
        scaffoldState = scaffoldState
    )

}

/**
 * Displays the Home screen.
 *
 * Stateless composable is not coupled to any specific state management.
 *
 * @param uiState (state) the data to show on the screen
 * @param navigateToAnime (event) request navigation to Anime details screen
 * @param openDrawer (event) request opening the app drawer
 * @param scaffoldState (state) state for the [Scaffold] component on this screen
 */
@ExperimentalCoilApi
@Composable
fun HomeScreen(
    uiAction: (UiAction) -> Unit,
    uiState: StateFlow<UiState>,
    navigateToAnime: (Long) -> Unit,
    openDrawer: () -> Unit,
    scaffoldState: ScaffoldState,
) {
    val scrollState = rememberLazyListState()
    val animeList = uiState.map { it.pagingData }.distinctUntilChanged()
    val lazyPagingItems: LazyPagingItems<Anime> = animeList.collectAsLazyPagingItems()

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = {
            SnackbarHost(
                hostState = it,
                modifier = Modifier.systemBarsPadding()
            )
        },
        topBar = { HomeScreenTopBar(uiState, uiAction, scrollState, openDrawer) }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        SwipeRefresh(
            state = rememberSwipeRefreshState(lazyPagingItems.loadState.refresh is LoadState.Loading),
            onRefresh = { lazyPagingItems.refresh() },
            content = {
                EmptyOrContent(
                    uiState,
                    uiAction,
                    lazyPagingItems = lazyPagingItems,
                    navigateToAnime = navigateToAnime,
                    modifier = modifier,
                    scrollState = scrollState,
                    scaffoldState = scaffoldState
                )
            },
        )

    }

}

@ExperimentalCoilApi
@Composable
private fun EmptyOrContent(
    uiState: StateFlow<UiState>,
    uiAction: (UiAction) -> Unit,
    lazyPagingItems: LazyPagingItems<Anime>,
    navigateToAnime: (Long) -> Unit,
    modifier: Modifier = Modifier,
    scrollState: LazyListState,
    scaffoldState: ScaffoldState
) {
    when {
        lazyPagingItems.loadState.refresh == LoadState.Loading -> {
            FullScreenLoading()
        }
        lazyPagingItems.itemCount == 0 -> {
            FullScreenEmptyListContent("No Anime found with \"${uiState.value.query}\"") {
                Button(
                    onClick = { lazyPagingItems.refresh() },
                    // Uses ButtonDefaults.ContentPadding by default
                    contentPadding = PaddingValues(
                        start = 20.dp,
                        top = 12.dp,
                        end = 20.dp,
                        bottom = 12.dp
                    ),
                    modifier = Modifier.padding(vertical = 24.dp)
                ) {
                    Text("Retry")
                }

            }
        }
        else -> {
            AnimeList(
                uiState,
                uiAction,
                lazyPagingItems = lazyPagingItems,
                navigateToAnime = navigateToAnime,
                modifier = modifier,
                scrollState = scrollState,
                scaffoldState = scaffoldState
            )
        }
    }
}


/**
 * Full screen circular progress indicator
 */
@Composable
private fun FullScreenEmptyListContent(text: String = "", content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
        )
        content()
    }
}


@ExperimentalCoilApi
@Composable
private fun AnimeList(
    uiState: StateFlow<UiState>,
    onScrollChange: (UiAction.Scroll) -> Unit,
    lazyPagingItems: LazyPagingItems<Anime>,
    navigateToAnime: (Long) -> Unit,
    modifier: Modifier = Modifier,
    scrollState: LazyListState,
    scaffoldState: ScaffoldState
) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier.padding(horizontal = 12.dp),
        state = scrollState,
        contentPadding = rememberInsetsPaddingValues(
            insets = LocalWindowInsets.current.systemBars,
            applyTop = false
        )
    ) {
        items(lazyPagingItems) { item ->
            item?.let {
                AnimeCard(anime = item, onClick = { navigateToAnime(item.id!!) })
            }
        }
        lazyPagingItems.apply {
            when {
                loadState.append is LoadState.Loading -> {
                    item { FullScreenLoading() }
                }
                loadState.append is LoadState.Error -> {
                    item {
                        FullScreenEmptyListContent("No Anime to display") {
                            Button(
                                onClick = { lazyPagingItems.retry() },
                                // Uses ButtonDefaults.ContentPadding by default
                                contentPadding = PaddingValues(
                                    start = 20.dp,
                                    top = 12.dp,
                                    end = 20.dp,
                                    bottom = 12.dp
                                ),
                               modifier = Modifier.padding(vertical = 24.dp)
                            ) {
                                Text("Retry")
                            }

                        }
                    }
                }
                loadState.refresh is LoadState.Error -> {
                    val errorState = loadState.source.refresh as? LoadState.Error
                        ?: loadState.refresh as? LoadState.Error

                    item {
                        val errorMessageText = errorState?.error.toString()
                        val retryMessageText = stringResource(id = R.string.retry)

                        LaunchedEffect(errorMessageText, retryMessageText, scaffoldState) {
                            val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                                message = errorMessageText,
                                actionLabel = retryMessageText
                            )
                            if (snackbarResult == SnackbarResult.ActionPerformed) {
                                lazyPagingItems.retry()
                            }
                        }

                        FullScreenEmptyListContent(text = errorState?.error.toString()) {
                            Button(
                                onClick = { lazyPagingItems.retry() },
                                // Uses ButtonDefaults.ContentPadding by default
                                contentPadding = PaddingValues(
                                    start = 20.dp,
                                    top = 12.dp,
                                    end = 20.dp,
                                    bottom = 12.dp
                                ),
                                modifier = Modifier.padding(vertical = 24.dp)
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }
            }
        }
    }
}