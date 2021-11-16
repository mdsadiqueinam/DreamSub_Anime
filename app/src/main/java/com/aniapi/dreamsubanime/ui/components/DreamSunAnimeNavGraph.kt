package com.aniapi.dreamsubanime.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.paging.ExperimentalPagingApi
import coil.annotation.ExperimentalCoilApi
import com.aniapi.dreamsubanime.viewModels.AnimeViewModel
import com.aniapi.dreamsubanime.viewModels.AnimeViewModel.Companion.ANIME_ID_KEY
import com.aniapi.dreamsubanime.viewModels.SearchAnimeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

object MainDestinations {
    const val HOME_ROUTE = "home"
    const val ANIME = "anime"
}


@ExperimentalFoundationApi
@ExperimentalCoilApi
@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@Composable
fun DreamSubAnimeNavGraph(
    navController: NavHostController = rememberNavController(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    startDestination: String = MainDestinations.HOME_ROUTE,
    startPlayer: (animeId: Long, episodeNumber: Int, animeTitle: String) -> Unit
) {
    val actions = remember(navController) { MainActions(navController) }
    val coroutineScope = rememberCoroutineScope()
    val openDrawer: () -> Unit = { coroutineScope.launch { scaffoldState.drawerState.open() } }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(MainDestinations.HOME_ROUTE) {
            // Creates a ViewModel from the current BackStackEntry
            // Available in the androidx.hilt:hilt-navigation-compose artifact
            val viewModel = hiltViewModel<SearchAnimeViewModel>()
            HomeScreen(
                viewModel = viewModel,
                navigateToAnime = actions.navigateToAnime,
                openDrawer = openDrawer
            )
        }
        composable(
            route = "${MainDestinations.ANIME}/{$ANIME_ID_KEY}",
            arguments = listOf(navArgument(ANIME_ID_KEY) { type = NavType.LongType })
        ) {
            val viewModel = hiltViewModel<AnimeViewModel>()
            AnimeScreen(
                viewModel = viewModel,
                onBack = actions.upPress,
                startPlayer = startPlayer
            )
        }
    }
}

/**
 * Models the navigation actions in the app.
 */
class MainActions(navController: NavHostController) {
    val navigateToAnime: (Long) -> Unit = { animeId: Long ->
        navController.navigate("${MainDestinations.ANIME}/$animeId")
    }
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}
