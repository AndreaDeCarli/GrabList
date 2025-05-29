package com.example.grablist.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.grablist.ui.NavRoute


@Composable
fun MainBottomAppBar(navController: NavController, active: Int){
    NavigationBar (
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        windowInsets = NavigationBarDefaults.windowInsets
    ) {
        NavigationBarItem(
            onClick = { navController.navigate(NavRoute.HomeScreen) },
            selected =  active == 1 ,
            icon = { Icon(Icons.Outlined.Home, "Home") }
        )
        NavigationBarItem(
            onClick = { navController.navigate(NavRoute.Favorites) },
            selected =  active == 2 ,
            icon = { Icon(Icons.Outlined.FavoriteBorder, "Favorite") }
        )
        NavigationBarItem(
            onClick = { navController.navigate(NavRoute.Profile) },
            selected =  active == 3 ,
            icon = { Icon(Icons.Outlined.AccountCircle, "Profile") }
        )
    }
}