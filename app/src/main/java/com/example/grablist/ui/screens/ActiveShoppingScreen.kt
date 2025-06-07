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
import com.example.grablist.ui.composables.GenericAlertDialog
import com.example.grablist.ui.composables.LazyCheckProductsColumn
import com.example.grablist.ui.viewmodels.ProductState
import com.example.grablist.ui.viewmodels.ProductsInListState
import com.example.grablist.ui.viewmodels.SettingsState
import com.example.grablist.ui.viewmodels.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveShoppingScreen (
    navController: NavController,
    state: ProductsInListState,
    shopList: ShopList,
    settingsViewModel: SettingsViewModel
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
            navController = navController,
            state = state,
            modifier = Modifier.padding(innerPadding),
            settingsViewModel = settingsViewModel)

        if (showQuitAlert){
            GenericAlertDialog(
                title = stringResource(R.string.quit_shopping_title),
                text = stringResource(R.string.quit_shopping_text),
                confirmText = stringResource(R.string.confirm),
                confirmAction = {
                    showQuitAlert = false
                    navController.navigateUp() },
                dismissText = stringResource(R.string.dismiss),
                dismissAction = { showQuitAlert = false },
                onDismissRequest = { showQuitAlert = false },
                icon = Icons.AutoMirrored.Outlined.ExitToApp
            )
        }
    }
}

