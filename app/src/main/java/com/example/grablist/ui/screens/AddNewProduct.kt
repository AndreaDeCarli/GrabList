package com.example.grablist.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.IconToggleButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.grablist.R
import com.example.grablist.ui.composables.MainTopAppBar
import com.example.grablist.ui.viewmodels.AddProductActions
import com.example.grablist.ui.viewmodels.AddProductState
import com.example.grablist.ui.viewmodels.AddShopListActions
import com.example.grablist.ui.viewmodels.AddShopListState
import com.example.grablist.utils.rememberCameraLauncher
import com.example.grablist.utils.saveImageToStorage

@Composable
fun AddNewProduct (
    state: AddProductState,
    actions: AddProductActions,
    onSubmit: () -> Unit,
    navController: NavController,
    lockFavorites: Boolean
){

    val ctx = LocalContext.current
    val cameraLauncher = rememberCameraLauncher(
        onPictureTaken = {
            imageUri -> actions.setImageUri(saveImageToStorage(imageUri,ctx.contentResolver))
        }
    )
    if (lockFavorites){
        actions.setFavorite(true)
    }

    Scaffold (
        topBar = { MainTopAppBar(navController, stringResource(id = R.string.new_product_title), true) },
        floatingActionButton = { FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.tertiary,
            onClick = {
                if (!state.canSubmit) return@FloatingActionButton
                onSubmit()
                navController.navigateUp()
            },
            shape = CircleShape
        ) {
            Icon(Icons.Outlined.Check, "Add")
        } }
    ) { innerPadding ->
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            OutlinedTextField(
                onValueChange = actions::setName,
                value = state.name,
                label = { Text(stringResource(id = R.string.title_generic)) },
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth())

            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "${ stringResource(R.string.favs_title) }: ")
                IconToggleButton(
                    enabled = !lockFavorites,
                    checked = state.favorite,
                    onCheckedChange = actions::setFavorite,
                    modifier = Modifier.padding(12.dp),
                    colors = IconToggleButtonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        disabledContainerColor = MaterialTheme.colorScheme.primary,
                        disabledContentColor = MaterialTheme.colorScheme.secondary,
                        checkedContainerColor = MaterialTheme.colorScheme.primary,
                        checkedContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    if (state.favorite) {
                        Icon(Icons.Filled.Favorite, "FavoriteFull")
                    }
                    else{
                        Icon(Icons.Outlined.FavoriteBorder, "FavoriteEmpty")
                    }
                }
            }
            Button(
                onClick = cameraLauncher::captureImage,

                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.tertiary,
                    disabledContentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {

                Text(stringResource(R.string.take_pic))
                Icon(Icons.Filled.CameraAlt, "camera")
            }

        }
    }
}