package com.example.grablist.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.grablist.R
import com.example.grablist.data.database.Theme
import com.example.grablist.ui.composables.MainTopAppBar
import com.example.grablist.ui.viewmodels.SettingsState
import com.example.grablist.ui.viewmodels.SettingsViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    state: SettingsState,
    settingsViewModel: SettingsViewModel
){
    Scaffold(
        topBar = { MainTopAppBar(navController = navController, stringResource(R.string.settings_title), goBack = false) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigateUp() },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Icon(Icons.Filled.Check, "Confirm")
            }
        }
    ) { innerPadding->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            item {
                Column(Modifier.selectableGroup()) {
                    Theme.entries.forEach { theme ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .selectable(
                                    selected = (theme == state.theme),
                                    onClick = { settingsViewModel.setTheme(theme) },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (theme == state.theme),
                                onClick = null
                            )
                            Text(
                                text = theme.toString(),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            }
            item {
                OutlinedTextField(
                    onValueChange = { settingsViewModel.setUsername(it) },
                    value = state.username,
                    label = { Text(stringResource(id = R.string.title_generic)) },
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth())
            }
        }

    }
}