package com.aniapi.dreamsubanime.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aniapi.dreamsubanime.R
import com.aniapi.dreamsubanime.utils.isScrolled
import com.aniapi.dreamsubanime.viewModels.UiAction
import com.aniapi.dreamsubanime.viewModels.UiState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun HomeScreenTopBar(
    uiState: StateFlow<UiState>,
    onQueryChange: (UiAction.Search) -> Unit,
    scrollState: LazyListState,
    openDrawer: () -> Unit,
) {
    var showSearch by remember { mutableStateOf(false) }
    val backgroundColor = MaterialTheme.colors.surface
    val elevation = if (!scrollState.isScrolled) 0.dp else 14.dp

    if (showSearch) {
        TopBarWithSearchField(
            uiState = uiState,
            onQueryChange = onQueryChange,
            cancelSearch = { showSearch = false },
            backgroundColor = backgroundColor,
            elevation = elevation
        )
    } else {
        TopBarWithTitle(
            showSearch = { showSearch = true },
            openDrawer = openDrawer,
            backgroundColor = backgroundColor,
            elevation = elevation
        )
    }
}

@Composable
private fun TopBarWithSearchField(
    uiState: StateFlow<UiState>,
    onQueryChange: (UiAction.Search) -> Unit,
    cancelSearch: () -> Unit,
    backgroundColor: Color,
    elevation: Dp
) {
    var query by remember { mutableStateOf(uiState.value.query) }

    InsetAwareTopAppBar(
        backgroundColor = backgroundColor,
        elevation = elevation,
        contentPadding = PaddingValues(0.dp)
    ) {
        TextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text(text = "Search Anime") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onQueryChange(UiAction.Search(query = query))
                }
            ),
            modifier = Modifier.fillMaxSize(),
            leadingIcon = {
                IconButton(
                    onClick = {
                        cancelSearch()
                        query = ""
                        onQueryChange(UiAction.Search(query = query))
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.cancel_search)
                    )
                }
            }
        )
    }
}

//@Preview
//@Composable
//fun TopBarWithSearchPreview() {
//    TopBarWithSearchField(
//        onValueChange = {},
//        onQueryChange = {},
//        query = "",
//        cancelSearch = {},
//        backgroundColor = MaterialTheme.colors.surface,
//        elevation = 0.dp,
//    )
//}

@Composable
private fun TopBarWithTitle(
    showSearch: () -> Unit,
    openDrawer: () -> Unit,
    backgroundColor: Color,
    elevation: Dp
) {
    InsetAwareTopAppBar(
        title = {
            AppWordMark()
        },
        navigationIcon = {
            IconButton(onClick = openDrawer) {
                Icon(
                    painter = painterResource(R.drawable.ic_app_logo),
                    contentDescription = stringResource(R.string.cd_open_navigation_drawer),
                    tint = MaterialTheme.colors.primary
                )
            }
        },
        actions = {
            IconButton(
                onClick = showSearch
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(R.string.cd_search)
                )
            }
        },
        backgroundColor = backgroundColor,
        elevation = elevation
    )
}