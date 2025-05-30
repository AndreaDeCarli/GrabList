package com.example.grablist.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.grablist.data.database.ShopList
import com.example.grablist.ui.composables.MainTopAppBar
import com.example.grablist.ui.viewmodels.ProductsInShopListViewModel
import com.example.grablist.ui.viewmodels.ProductsState

@Composable
fun ShopListDetailsScreen(
    navController: NavController,
    shopList: ShopList,
    vm: ProductsInShopListViewModel,
    state: ProductsState) {
    Scaffold(
        topBar = { MainTopAppBar(navController = navController, title = shopList.title, goBack = true) }
    ) { innerPadding ->
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            Surface (
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.padding(innerPadding).weight(0.2F)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(shopList.date)
                    Button(
                        onClick = {},
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(),
                        elevation = ButtonDefaults.buttonElevation(12.dp),
                    ) {
                        Text("Start")
                    }
                }
            }


        }
    }
}