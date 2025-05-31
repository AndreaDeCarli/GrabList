package com.example.grablist.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.grablist.R
import com.example.grablist.ui.composables.LazyProductColumn
import com.example.grablist.ui.composables.MainBottomAppBar
import com.example.grablist.ui.composables.MainTopAppBar
import com.example.grablist.ui.viewmodels.ProductsViewModel

@Composable
fun Favorites(navController: NavController, vm: ProductsViewModel){
    Scaffold(
        topBar = { MainTopAppBar(
            navController = navController,
            title = stringResource(id = R.string.favs_title),
            goBack = false) },
        bottomBar = { MainBottomAppBar(
            navController = navController,
            active = 2
        ) }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding),
            color = MaterialTheme.colorScheme.surface
        ) {
            val productsFavorites by vm.favorites.collectAsStateWithLifecycle()
            LazyProductColumn(
                navController = navController,
                products = productsFavorites.products,
                shopList = null,
                vm = vm
            )
        }
    }
}