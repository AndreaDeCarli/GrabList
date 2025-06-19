package com.example.grablist.ui.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.grablist.R
import com.example.grablist.data.database.Theme
import com.example.grablist.ui.composables.CustomDivider
import com.example.grablist.ui.composables.CustomTextField
import com.example.grablist.ui.composables.GenericAlertDialog
import com.example.grablist.ui.composables.MainTopAppBar
import com.example.grablist.ui.viewmodels.SettingsState
import com.example.grablist.ui.viewmodels.SettingsViewModel
import com.example.grablist.utils.PermissionStatus
import com.example.grablist.utils.deleteImageFromFiles
import com.example.grablist.utils.rememberCameraLauncher
import com.example.grablist.utils.rememberMultiplePermissions
import com.example.grablist.utils.saveImageToInternalStorage
import com.example.grablist.utils.saveImageToStorage

@Composable
fun SettingsScreen(
    navController: NavController,
    state: SettingsState,
    settingsViewModel: SettingsViewModel
){

    var showPermissionAlert by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }

    val ctx = LocalContext.current

    val cameraLauncher = rememberCameraLauncher(
        onPictureTaken = { imageUri ->
            settingsViewModel.setProfilePicUri(imageUri)
            saveImageToStorage(imageUri, ctx.contentResolver)
        }
    )

    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { imageUri: Uri? ->
        settingsViewModel.setProfilePicUri(requireNotNull(saveImageToInternalStorage(ctx, requireNotNull(imageUri))))
    }

    val permissions = rememberMultiplePermissions(
        listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    ) { statuses ->
        when {
            statuses.any { it.value == PermissionStatus.Granted } -> {}
            statuses.all { it.value == PermissionStatus.PermanentlyDenied } ->
                showPermissionAlert = true
        }
    }

    val buttonColors = ButtonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = MaterialTheme.colorScheme.inverseSurface,
        disabledContentColor = MaterialTheme.colorScheme.inverseOnSurface
    )

    BackHandler {  } /*NO GOING BACK*/

    Scaffold(
        topBar = { MainTopAppBar(
            navController = navController,
            title = stringResource(R.string.settings_title),
            goBack = false,
            options = false) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (state.username != ""){
                        navController.navigateUp()
                    }else{
                        showError = true
                    }
                          },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onBackground
            ) {
                Icon(Icons.Filled.Check, "Confirm")
            }
        }
    ) { innerPadding->

        if (showPermissionAlert){
            GenericAlertDialog(
                title = stringResource(R.string.permissions_title),
                text = stringResource(R.string.permissions_text),
                confirmText = stringResource(R.string.confirm),
                confirmAction = {
                    ctx.startActivity(
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", ctx.packageName, null)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK })

                    showPermissionAlert = false},
                dismissText = stringResource(R.string.dismiss),
                dismissAction = { showPermissionAlert = false },
                onDismissRequest = { showPermissionAlert = false },
                icon = Icons.Filled.Error
            )
        }

        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                CustomDivider(stringResource(R.string.settings_title_name))
                CustomTextField(state.username, { settingsViewModel.setUsername(it); showError = false }, showError)
                CustomDescription(R.string.settings_desc_name)
            }
            item {
                CustomDivider(stringResource(R.string.settings_title_theme))

                Row(Modifier.selectableGroup()) {
                    Theme.entries.forEach { theme ->
                        Row(
                            Modifier
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
                                text = stringResource(theme.label),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
                CustomDescription(R.string.settings_desc_theme)
            }
            item {
                CustomDivider(stringResource(R.string.settings_title_progress))

                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "${stringResource(R.string.completed_progress)}: ${state.progress}")
                    Button(
                        enabled = state.progress != 0,
                        onClick = { showResetDialog = true }
                    ) {
                        Text(text = stringResource(R.string.reset_progress))
                    }
                }
                CustomDescription(R.string.settings_desc_progress)

            }
            item {
                CustomDivider(stringResource(R.string.settings_title_pic))
                Row(modifier = Modifier
                    .height(170.dp)
                    .padding(vertical = 15.dp)
                    .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically) {
                    Column (
                        modifier = Modifier
                            .weight(0.6F)
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Button(
                            enabled = state.profilePicUri == Uri.EMPTY,
                            modifier = Modifier
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
                            enabled = state.profilePicUri == Uri.EMPTY,
                            modifier = Modifier
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
                        Button(
                            enabled = state.profilePicUri != Uri.EMPTY,
                            modifier = Modifier
                                .size(width = 220.dp, height = 40.dp),
                            onClick = {
                                deleteImageFromFiles(ctx, state.profilePicUri)
                                settingsViewModel.setProfilePicUri(Uri.EMPTY)
                            },
                            colors = buttonColors
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(stringResource(R.string.delete_pic), modifier = Modifier.weight(0.8F), textAlign = TextAlign.Center)
                                Icon(Icons.Filled.Delete, "search", modifier = Modifier.weight(0.2F))
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(0.4F)
                            .padding(horizontal = 10.dp)
                            .size(170.dp),
                        contentAlignment = Alignment.TopEnd,
                    ) {
                        if (state.profilePicUri != Uri.EMPTY) {
                            AsyncImage(
                                state.profilePicUri,
                                contentDescription = "profileImage",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .shadow(3.dp, RoundedCornerShape(20.dp))
                                    .clip(RoundedCornerShape(20.dp))
                                    .fillMaxSize(),
                            )
                        } else {
                            Image(
                                imageVector = Icons.Filled.AccountCircle,
                                contentDescription = "ProfileImage",
                                modifier = Modifier.fillMaxSize(),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                            )
                        }
                    }
                }

                CustomDescription(R.string.settings_desc_pic)
            }
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
        if (showResetDialog){
            GenericAlertDialog(
                title = stringResource(R.string.reset_progress),
                text = stringResource(R.string.reset_progress_text),
                confirmText = stringResource(R.string.confirm),
                confirmAction = {
                    settingsViewModel.resetProgress()
                    showResetDialog = false },
                dismissText = stringResource(R.string.dismiss),
                dismissAction = { showResetDialog = false },
                onDismissRequest = { showResetDialog = false },
                icon = Icons.Filled.Warning
            )
        }
    }
}


@Composable
fun CustomDescription(stringId: Int){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 5.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = "information",
            tint = MaterialTheme.colorScheme.inverseOnSurface,
            modifier = Modifier.weight(0.10F)
        )
        Text(
            text = stringResource(stringId),
            modifier = Modifier
                .weight(0.90F),
            color = MaterialTheme.colorScheme.inverseOnSurface,
            fontSize = 12.sp,
            lineHeight = 14.sp
        )
    }
}