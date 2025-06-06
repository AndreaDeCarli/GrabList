package com.example.grablist.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.grablist.R
import com.example.grablist.data.database.Product
import com.example.grablist.data.database.ShopList
import com.example.grablist.ui.composables.LazyCheckProductsColumn
import com.example.grablist.ui.viewmodels.ProductState
import com.example.grablist.ui.viewmodels.ProductsInListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveShoppingScreen (
    navController: NavController,
    state: ProductsInListState,
    shopList: ShopList
    ) {

    var showQuitAlert by remember { mutableStateOf(false) }


    BackHandler {  } /*NO GOING BACK*/

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(shopList.title) },
                modifier = Modifier,
                actions = { Button(
                    onClick = { showQuitAlert = true }
                ) {
                    Text("Quit")
                    Icon(Icons.AutoMirrored.Outlined.ExitToApp, "exit")
                } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary),
            )
        }
    ) { innerPadding ->
        LazyCheckProductsColumn(
            state = state,
            modifier = Modifier.padding(innerPadding))

        if (showQuitAlert){
            AlertDialog(
                icon = {
                    Icon(
                        Icons.AutoMirrored.Outlined.ExitToApp, contentDescription = "Exit",
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                },
                title = {
                    Text(
                        text = stringResource(R.string.delete_shoplist_dialog_title),
                        color = MaterialTheme.colorScheme.onBackground)
                },
                text = {
                    Text(
                        text = stringResource(R.string.delete_shoplist_dialog_text),
                        color = MaterialTheme.colorScheme.onBackground)
                },
                onDismissRequest = {
                    showQuitAlert = false
                },
                confirmButton = {
                    TextButton(
                        onClick = { showQuitAlert = false
                            navController.navigateUp() },
                        colors = ButtonColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.tertiary,
                            disabledContainerColor = MaterialTheme.colorScheme.background,
                            disabledContentColor = MaterialTheme.colorScheme.background
                        )
                    ) {
                        Text(stringResource(R.string.confirm))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showQuitAlert = false },
                        colors = ButtonColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.tertiary,
                            disabledContainerColor = MaterialTheme.colorScheme.background,
                            disabledContentColor = MaterialTheme.colorScheme.background
                        )
                    ) {
                        Text(stringResource(R.string.dismiss))
                    }
                },
                containerColor = MaterialTheme.colorScheme.background,
            )
        }
    }
}

