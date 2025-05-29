package com.example.grablist.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(navController: NavController, title: String, goBack: Boolean){
    TopAppBar(
        title = { Text(title) },
        navigationIcon = { if (goBack) {
            IconButton( onClick = {navController.navigateUp()}) {
                Icon(Icons.Filled.ArrowBackIosNew, "Back")
            }
        } },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        actions = {
            IconButton( onClick = {  }) {
                Icon(Icons.Filled.Settings, "Settings")
            }
        }
    )
}