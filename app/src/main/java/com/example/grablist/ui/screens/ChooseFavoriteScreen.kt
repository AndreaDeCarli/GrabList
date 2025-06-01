package com.example.grablist.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.grablist.R
import com.example.grablist.data.database.ShopList
import com.example.grablist.ui.composables.LazyProductColumn
import com.example.grablist.ui.composables.MainTopAppBar
import com.example.grablist.ui.viewmodels.ProductsViewModel

@Composable
fun ChooseFavoriteScreen(
    navController: NavController,
    vm: ProductsViewModel,
    shopList: ShopList
){
    val productsFavorites by vm.favorites.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            MainTopAppBar(
                navController,
                stringResource(R.string.select_favorite),
                true) }
    ) { innerPadding ->
        LazyProductColumn(
            showFavorites = false,
            products = productsFavorites.products,
            shopList = null,
            vm = vm,
            modifier = Modifier.padding(innerPadding),
            onClick = { product ->
                vm.addReference(product, shopList)
                navController.navigateUp()
            },
            noInteractions = true
        )
    }
}