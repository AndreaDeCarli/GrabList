package com.example.grablist.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.grablist.ui.composables.MainBottomAppBar
import com.example.grablist.ui.composables.MainTopAppBar

@Composable
fun Favorites(navController: NavController){
    Scaffold(
        topBar = { MainTopAppBar(
            navController = navController,
            title = "Favorites",
            goBack = false) },
        bottomBar = { MainBottomAppBar(
            navController = navController,
            active = 2
        ) }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {

        }
    }
}