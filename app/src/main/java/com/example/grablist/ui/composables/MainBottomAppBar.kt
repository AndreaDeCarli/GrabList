package com.example.grablist.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.grablist.ui.NavRoute


@Composable
fun MainBottomAppBar(navController: NavController, active: Int){
    val colors = NavigationBarItemColors(
        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
        selectedTextColor = MaterialTheme.colorScheme.onPrimary,
        selectedIndicatorColor = MaterialTheme.colorScheme.primary,
        unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
        unselectedTextColor = MaterialTheme.colorScheme.onPrimary,
        disabledIconColor = MaterialTheme.colorScheme.secondary,
        disabledTextColor = MaterialTheme.colorScheme.secondary
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        windowInsets = NavigationBarDefaults.windowInsets,
    ) {
        NavigationBarItem(
            onClick = { navController.navigate(NavRoute.HomeScreen) },
            colors = colors,
            selected =  active == 1 ,
            icon = {
                if (active != 1){
                    Icon(Icons.Outlined.Home, "Home")
                }else{
                    Icon(Icons.Filled.Home, "Home")
                }
            }
        )
        NavigationBarItem(
            onClick = { navController.navigate(NavRoute.Favorites) },
            colors = colors,
            selected =  active == 2 ,
            icon = {
                if (active != 2){
                    Icon(Icons.Outlined.FavoriteBorder, "Favorite")
                }else{
                    Icon(Icons.Filled.Favorite, "Favorite")
                }
            }
        )
        NavigationBarItem(
            onClick = { navController.navigate(NavRoute.Profile) },
            colors = colors,
            selected =  active == 3 ,
            icon = {
                if (active != 3){
                    Icon(Icons.Outlined.AccountCircle, "Profile")
                }else{
                    Icon(Icons.Filled.AccountCircle, "Profile")
                }
            }

        )
    }
}