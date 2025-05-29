package com.example.grablist.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.grablist.ui.composables.AddNewShopListButton
import com.example.grablist.ui.composables.MainBottomAppBar
import com.example.grablist.ui.composables.MainTopAppBar

@Composable
fun HomeScreen (navController: NavController){
    Scaffold(
        topBar = { MainTopAppBar(navController = navController,title = "Home", goBack = false) },
        bottomBar = { MainBottomAppBar(active = 1, navController = navController) },
        floatingActionButton = { AddNewShopListButton(navController) }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text("")
        }
    }
}
