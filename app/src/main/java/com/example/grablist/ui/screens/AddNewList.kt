package com.example.grablist.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.grablist.R
import com.example.grablist.ui.composables.MainTopAppBar
import com.example.grablist.ui.viewmodels.AddShopListActions
import com.example.grablist.ui.viewmodels.AddShopListState

@Composable
fun AddNewList (state: AddShopListState, actions: AddShopListActions, onSubmit: () -> Unit, navController: NavController){
    Scaffold (
        topBar = { MainTopAppBar(navController, stringResource(id = R.string.new_list_title), true) },
        floatingActionButton = { FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.tertiary,
            onClick = {
                if (!state.canSubmit) return@FloatingActionButton
                onSubmit()
                navController.navigateUp()
            }
        ) {
            Icon(Icons.Outlined.Check, "Add")
        } }
    ) { innerPadding ->
        Column (
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            OutlinedTextField(
                onValueChange = actions::setTitle,
                value = state.title,
                label = { Text(stringResource(id = R.string.title_generic)) },
                modifier = Modifier.padding(12.dp).fillMaxWidth())

            OutlinedTextField(
                onValueChange = actions::setDate,
                value = state.date,
                label = { Text(stringResource(id = R.string.date_generic)) },
                modifier = Modifier.padding(12.dp).fillMaxWidth())
        }
    }
}