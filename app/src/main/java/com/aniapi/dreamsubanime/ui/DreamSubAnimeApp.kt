package com.aniapi.dreamsubanime.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.ExperimentalPagingApi
import coil.annotation.ExperimentalCoilApi
import com.aniapi.dreamsubanime.ui.components.AppDrawer
import com.aniapi.dreamsubanime.ui.components.DreamSubAnimeNavGraph
import com.aniapi.dreamsubanime.ui.components.MainDestinations
import com.aniapi.dreamsubanime.ui.theme.DreamSubAnimeTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@ExperimentalCoilApi
@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@Composable
fun DreamSubAnimeApp(
    startPlayer: (animeId: Long, episodeNumber: Int, animeTitle: String) -> Unit
) {
    DreamSubAnimeTheme {
        ProvideWindowInsets {
            val systemUiController = rememberSystemUiController()
            val darkIcons = MaterialTheme.colors.isLight
            SideEffect {
                systemUiController.setSystemBarsColor(Color.Transparent, darkIcons = darkIcons)
            }

            val navController = rememberNavController()
            val coroutineScope = rememberCoroutineScope()
            // This top level scaffold contains the app drawer, which needs to be accessible
            // from multiple screens. An event to open the drawer is passed down to each
            // screen that needs it.
            val scaffoldState = rememberScaffoldState()

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route ?: MainDestinations.HOME_ROUTE
            Scaffold(
                scaffoldState = scaffoldState,
                drawerContent = {
                    AppDrawer(
                        currentRoute = currentRoute,
                        navigateToHome = { navController.navigate(MainDestinations.HOME_ROUTE) },
                        closeDrawer = { coroutineScope.launch { scaffoldState.drawerState.close() } },
                    )
                }
            ) {
                DreamSubAnimeNavGraph(
                    navController = navController,
                    scaffoldState = scaffoldState,
                    startPlayer = startPlayer
                )
            }
        }
    }
}