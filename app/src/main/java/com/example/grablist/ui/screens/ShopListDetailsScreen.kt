package com.example.grablist.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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

@Composable
fun ShopListDetailsScreen(
    navController: NavController,
    shopList: ShopList,
    vm: ProductsViewModel,
    state: ProductsInListState) {
    Scaffold(
        topBar = { MainTopAppBar(navController = navController, title = shopList.title, goBack = true) },
        floatingActionButton = {
            var expanded by remember { mutableStateOf(false) }
            FloatingActionButton(
                onClick = { expanded = !expanded/*navController.navigate(NavRoute.AddNewProduct(shopList.shopListId))*/ },
                elevation = FloatingActionButtonDefaults.elevation(4.dp),
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.surface
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
                        leadingIcon = { Icon(Icons.Filled.ShoppingCart, "Shop") },
                        onClick = {
                            expanded = false
                            navController.navigate(NavRoute.AddNewProduct(shopList.shopListId))
                        }
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.onBackground)
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.select_favorite)) },
                        leadingIcon = { Icon(Icons.Filled.Favorite, "Favorite") },
                        onClick = {
                            expanded = false
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Surface (
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .padding(innerPadding)
                    .weight(0.25F)
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
                        Text(text = "${stringResource(R.string.title_generic)} : ${shopList.title}",
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1)
                        Text(text = "${stringResource(R.string.date_generic)} : ${shopList.date}",
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1)
                    }
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                            .weight(0.40F),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(),
                        elevation = ButtonDefaults.buttonElevation(3.dp),
                    ) {
                        Text(stringResource(R.string.start))
                        Icon(Icons.Filled.PlayArrow, "Start")
                    }
                }
            }
            LazyProductColumn(
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .weight(0.75F)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                navController = navController,
                shopList = shopList,
                products = state.products,
                vm = vm,
                showFavorites = true
            )
        }
    }
}