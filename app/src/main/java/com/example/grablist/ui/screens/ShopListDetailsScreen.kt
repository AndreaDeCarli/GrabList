package com.example.grablist.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.grablist.R
import com.example.grablist.data.database.ShopList
import com.example.grablist.ui.NavRoute
import com.example.grablist.ui.composables.LazyProductColumn
import com.example.grablist.ui.composables.MainTopAppBar
import com.example.grablist.ui.viewmodels.ProductsInListState
import com.example.grablist.ui.viewmodels.ProductsViewModel
import com.example.grablist.utils.MapComposable

@Composable
fun ShopListDetailsScreen(
    navController: NavController,
    shopList: ShopList,
    vm: ProductsViewModel,
    state: ProductsInListState) {

    var showMap by remember { mutableStateOf(false) }

    BackHandler {
        showMap = false
        navController.navigateUp()
    }

    Scaffold(
        topBar = { MainTopAppBar(navController = navController, title = shopList.title, goBack = true, additionalAction = { showMap = false }) },
        floatingActionButton = {
            var expanded by remember { mutableStateOf(false) }
            FloatingActionButton(
                onClick = { expanded = !expanded },
                elevation = FloatingActionButtonDefaults.elevation(4.dp),
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onBackground
            ) {
                if (!expanded){
                    Icon(Icons.Filled.Add, "Add")
                }else{
                    Icon(Icons.Filled.Close, "Back")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.new_product_title)) },
                        leadingIcon = { Icon(Icons.Filled.ShoppingCart, "Shop", tint = MaterialTheme.colorScheme.onBackground) },
                        onClick = {
                            expanded = false
                            navController.navigate(NavRoute.AddNewProduct(shopList.shopListId, false))
                        }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.onBackground)
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.select_favorite)) },
                        leadingIcon = { Icon(Icons.Filled.Favorite, "Favorite", tint = MaterialTheme.colorScheme.onBackground) },
                        onClick = {
                            expanded = false
                            navController.navigate(NavRoute.ChooseFavProduct(shopList.shopListId))
                        }
                    )
                }
            }
        }
    ) { innerPadding ->



        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Surface (
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .height(if (showMap) 130.dp else 100.dp)
                    .fillMaxWidth()

            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .weight(0.60F),
                        horizontalAlignment = Alignment.Start

                    ) {
                        if (shopList.location.name != ""){
                            Text(text = shopList.location.name,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = if (showMap) 2 else 1)
                        }else{
                            Text(text = shopList.title,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = if (showMap) 2 else 1)
                        }
                        if (shopList.date != ""){
                            Text(text = shopList.date,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = if (showMap) 2 else 1)
                        }

                    }
                    if (shopList.location.name != ""){
                        IconButton(onClick = { showMap = !showMap }) { Icon(imageVector = if (showMap) Icons.Filled.Info else Icons.Outlined.Info, "info") }
                    }
                    Button(
                        enabled = state.products.isNotEmpty(),
                        onClick = { navController.navigate(NavRoute.ActiveShopping(shopList.shopListId)) },
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                            .weight(0.40F),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                        elevation = ButtonDefaults.buttonElevation(3.dp),
                    ) {
                        Text(stringResource(R.string.start))
                        Icon(Icons.Filled.PlayArrow, "Start")
                    }
                }
            }

            if (showMap){
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .height(300.dp)
                        .fillMaxWidth()) {
                    MapComposable(shopList = shopList, modifier = Modifier.padding(20.dp).fillMaxSize())
                }
            }

            LazyProductColumn(
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .background(MaterialTheme.colorScheme.background),
                onClick = { product -> navController.navigate(NavRoute.ProductDetails(product.productId)) },
                shopList = shopList,
                products = state.products,
                vm = vm,
                showFavorites = true
            )
        }
    }
}