package com.example.grablist.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.grablist.R
import com.example.grablist.data.database.ShopList
import com.example.grablist.ui.composables.AddNewShopListButton
import com.example.grablist.ui.composables.LazyShopListColumn
import com.example.grablist.ui.composables.MainBottomAppBar
import com.example.grablist.ui.composables.MainTopAppBar
import com.example.grablist.ui.viewmodels.ShopListState
import com.example.grablist.ui.viewmodels.ShopListViewModel

@Composable
fun HomeScreen (navController: NavController, onDelete: (shopList: ShopList) -> Unit, state: ShopListState){
    Scaffold(
        topBar = { MainTopAppBar(
            navController = navController,
            title = stringResource(id = R.string.home_title),
            goBack = false)
                 },
        bottomBar = { MainBottomAppBar(
            active = 1,
            navController = navController)
                    },
        floatingActionButton = { AddNewShopListButton(navController) }
    ) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyShopListColumn(
                state = state,
                onDelete = onDelete,
                navController = navController
            )
        }
    }
}
