package com.example.grablist.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.grablist.R
import com.example.grablist.data.database.Product
import com.example.grablist.data.database.ShopList
import com.example.grablist.ui.NavRoute
import com.example.grablist.ui.composables.LazyProductColumn
import com.example.grablist.ui.composables.MainTopAppBar
import com.example.grablist.ui.viewmodels.ProductsViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ChooseFavoriteScreen(
    navController: NavController,
    vm: ProductsViewModel,
    products: List<Product>,
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
        val coroutineScope = rememberCoroutineScope()
        LazyProductColumn(
            showFavorites = false,
            products = productsFavorites.products,
            shopList = null,
            vm = vm,
            modifier = Modifier.padding(innerPadding),
            onClick = { product ->
                navController.navigateUp()
                coroutineScope.launch {
                    delay(300)
                    vm.addReference(product, shopList)
                }


            },
            noInteractions = true,
            canClick = {product -> product !in products }
        )
    }
}