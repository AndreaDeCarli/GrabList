package com.example.grablist.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.IconToggleButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.grablist.R
import com.example.grablist.ui.composables.CustomTextField
import com.example.grablist.ui.composables.GenericAlertDialog
import com.example.grablist.ui.composables.MainTopAppBar
import com.example.grablist.ui.viewmodels.AddProductActions
import com.example.grablist.ui.viewmodels.AddProductState
import com.example.grablist.utils.PermissionStatus
import com.example.grablist.utils.deleteImageFromFiles
import com.example.grablist.utils.rememberCameraLauncher
import com.example.grablist.utils.rememberMultiplePermissions
import com.example.grablist.utils.saveImageToInternalStorage
import com.example.grablist.utils.saveImageToStorage

@Composable
fun AddNewProduct (
    state: AddProductState,
    actions: AddProductActions,
    onSubmit: () -> Unit,
    navController: NavController,
    lockFavorites: Boolean,
    ctx: Context
){

    var showError by remember { mutableStateOf(false) }

    val cameraLauncher = rememberCameraLauncher(
        onPictureTaken = {
            imageUri -> actions.setImageUri(imageUri)
            saveImageToStorage(imageUri, ctx.contentResolver)
        }
    )

    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { imageUri: Uri? ->
            actions.setImageUri(requireNotNull(saveImageToInternalStorage(ctx, requireNotNull(imageUri))))
    }

    val permissions = rememberMultiplePermissions(
        listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    ) { statuses ->
        when {
            statuses.any { it.value == PermissionStatus.Granted } -> {}
            statuses.all { it.value == PermissionStatus.PermanentlyDenied } ->
                actions.setShowPermissionAlert(true)
        }
    }

    val buttonColors = ButtonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = MaterialTheme.colorScheme.inverseSurface,
        disabledContentColor = MaterialTheme.colorScheme.inverseOnSurface
    )

    BackHandler {
        if (state.imageUri != Uri.EMPTY){
            deleteImageFromFiles(ctx, state.imageUri)
        }
    }

    Scaffold (
        topBar = { MainTopAppBar(navController = navController,
            title = stringResource(id = R.string.new_product_title),
            goBack = true,
            additionalAction = {
                if (state.imageUri != Uri.EMPTY){
                    deleteImageFromFiles(ctx, state.imageUri)
                }
            })
                 },
        floatingActionButton = { FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onBackground,
            onClick = {
                if (!state.canSubmit) {
                    showError = true
                    return@FloatingActionButton
                }
                onSubmit()
                navController.navigateUp()
            },
            shape = CircleShape
        ) {
            Icon(Icons.Outlined.Check, "Add")
        } }
    ) { innerPadding ->

        if (lockFavorites){
            actions.setFavorite(true)
        }

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            CustomTextField(state.name, { actions.setName(it); showError = false }, showError)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
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
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        disabledContainerColor = MaterialTheme.colorScheme.inverseSurface,
                        disabledContentColor = MaterialTheme.colorScheme.inverseOnSurface,
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
                enabled = state.imageUri == Uri.EMPTY,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .size(width = 220.dp, height = 40.dp),
                onClick = {
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P){
                        permissions.launchPermissionRequest()
                        if (permissions.statuses[Manifest.permission.WRITE_EXTERNAL_STORAGE] == PermissionStatus.Granted){
                            cameraLauncher.captureImage()
                        }
                    }else{
                        cameraLauncher.captureImage()
                    } },
                colors = buttonColors
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(R.string.take_pic), modifier = Modifier.weight(0.8F), textAlign = TextAlign.Center)
                    Icon(Icons.Filled.CameraAlt, "camera", modifier = Modifier.weight(0.2F))
                }
            }

            Button(
                enabled = state.imageUri == Uri.EMPTY,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .size(width = 220.dp, height = 40.dp),
                onClick = {
                    launcher.launch("image/*")
                },
                colors = buttonColors
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(R.string.select_pic), modifier = Modifier.weight(0.8F), textAlign = TextAlign.Center)
                    Icon(Icons.Filled.Upload, "search", modifier = Modifier.weight(0.2F))
                }
            }

            if (state.imageUri != Uri.EMPTY){
                AsyncImage(
                    model = state.imageUri,
                    contentDescription = "image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(12.dp)
                        .size(180.dp, 180.dp)
                        .border(3.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(30.dp))
                        .clip(RoundedCornerShape(30.dp))
                )
            }
        }
        if (state.showPermissionAlert){
            GenericAlertDialog(
                title = stringResource(R.string.permissions_title),
                text = stringResource(R.string.permissions_text),
                confirmText = stringResource(R.string.confirm),
                confirmAction = {
                    ctx.startActivity(
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", ctx.packageName, null)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK })

                    actions.setShowPermissionAlert(false)},
                dismissText = stringResource(R.string.dismiss),
                dismissAction = { actions.setShowPermissionAlert(false) },
                onDismissRequest = { actions.setShowPermissionAlert(false) },
                icon = Icons.Filled.Error
            )
        }
    }
}