package com.example.grablist.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.grablist.ui.composables.MainBottomAppBar
import com.example.grablist.ui.composables.MainTopAppBar

@Composable
fun HomeScreen (){
    Scaffold(
        topBar = { MainTopAppBar(title = "Home", goBack = false) },
        bottomBar = { MainBottomAppBar(active = 1, navController = rememberNavController()) }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text("decaaaaaaa")
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview(){
    HomeScreen()
}