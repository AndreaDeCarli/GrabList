package com.example.grablist.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.grablist.ui.NavRoute

@Composable
fun AddNewShopListButton(navController: NavController){
    FloatingActionButton(
        onClick = { navController.navigate(NavRoute.AddNewList) },
        containerColor = MaterialTheme.colorScheme.surface,
        elevation = FloatingActionButtonDefaults.elevation()
    ) {
        Icon(Icons.Filled.Add, "Add")
    }
}