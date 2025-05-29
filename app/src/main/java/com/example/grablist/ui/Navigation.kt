package com.example.grablist.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.grablist.ui.screens.HomeScreen
import kotlinx.serialization.Serializable

sealed interface NavRoute {
    @Serializable data object HomeScreen : NavRoute
    @Serializable data object Favorites : NavRoute
    @Serializable data object Profile : NavRoute
    @Serializable data object AddNewList : NavRoute
}

@Composable
fun GrabListNavGraph(navController: NavHostController) {


    NavHost(navController = navController,
        startDestination = NavRoute.HomeScreen
    ) {
        composable<NavRoute.HomeScreen> {
            HomeScreen(navController)
        }
        composable<NavRoute.Favorites> {
            TODO()
        }
        composable<NavRoute.Profile> {
            TODO()
        }
    }
}