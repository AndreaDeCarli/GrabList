package com.example.grablist.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            FloatingActionButton(
                onClick = { navController.navigate(NavRoute.AddNewProduct(shopList.shopListId)) },
                elevation = FloatingActionButtonDefaults.elevation(4.dp)
            ) {
                Icon(Icons.Filled.Add, "Add")
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
                        modifier = Modifier.padding(20.dp).weight(0.60F),
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
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp).weight(0.40F),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(),
                        elevation = ButtonDefaults.buttonElevation(3.dp),
                    ) {
                        Text("Start")
                        Icon(Icons.Filled.PlayArrow, "Start")
                    }
                }
            }
            LazyProductColumn(
                modifier = Modifier.padding(horizontal = 12.dp).weight(0.75F),
                navController = navController,
                shopList = shopList,
                products = state.products,
                vm = vm,
                showFavorites = true
            )
        }
    }
}