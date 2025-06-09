package com.example.grablist.ui.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.grablist.R
import com.example.grablist.ui.composables.GenericAlertDialog
import com.example.grablist.ui.composables.MainTopAppBar
import com.example.grablist.ui.viewmodels.AddShopListActions
import com.example.grablist.ui.viewmodels.AddShopListState
import com.example.grablist.utils.PermissionStatus
import com.example.grablist.utils.addCalendarEvent
import com.example.grablist.utils.getPrimaryCalendarId
import com.example.grablist.utils.rememberMultiplePermissions
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AddNewList (state: AddShopListState, actions: AddShopListActions, onSubmit: () -> Unit, navController: NavController){

    val ctx = LocalContext.current

    val permissions = rememberMultiplePermissions(
        listOf(Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR)
    ) { statuses ->
        when {
            statuses.all { it.value == PermissionStatus.Granted } -> {
                actions.setSaveInCalender(true)
            }
            statuses.all { it.value == PermissionStatus.PermanentlyDenied } ->
                actions.setShowPermissionAlert(true)
        }
    }

    Scaffold (
        topBar = { MainTopAppBar(navController, stringResource(id = R.string.new_list_title), true) },
        floatingActionButton = { FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onBackground,
            onClick = {
                if (!state.canSubmit) return@FloatingActionButton
                onSubmit()
                if (state.saveInCalender){
                    addCalendarEvent(date = state.date,
                        ctx = ctx,
                        title = state.title,
                        description = "Remember to go shopping: ${state.title}",
                        calendarId = getPrimaryCalendarId(ctx)
                    )
                }
                navController.navigateUp()
            },
            shape = CircleShape
        ) {
            Icon(Icons.Outlined.Check, "Add")
        } }
    ) { innerPadding ->


        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            var showDatePicker by remember { mutableStateOf(false) }
            OutlinedTextField(
                onValueChange = actions::setTitle,
                value = state.title,
                label = { Text(stringResource(id = R.string.title_generic)) },
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth())

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically){
                Button(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 15.dp),
                    onClick = {
                        showDatePicker = true
                    }
                ) {
                    Text(stringResource(R.string.select_date))
                    Icon(Icons.Filled.CalendarMonth, "Calendar")
                }
                Text(text = state.date, modifier = Modifier.padding(horizontal = 10.dp, vertical = 15.dp),)
            }

            Row (modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
            ){
                Checkbox(
                    enabled = state.date != "",
                    checked = state.saveInCalender,
                    onCheckedChange = {
                        if (!state.saveInCalender) {
                            permissions.launchPermissionRequest()
                        }else{
                            actions.setSaveInCalender(false)
                        }
                    }
                )
                Text(stringResource(R.string.add_to_calendar))
            }

            if (showDatePicker){
                DatePickerModal(
                    onDismiss = { showDatePicker = false },
                    onDateSelected = { millis ->
                        actions.setDate(convertMillisToDate(requireNotNull(millis)))
                        showDatePicker = false})
            }

            var selected by remember { mutableIntStateOf(0) }
            val imagesIds = listOf(R.drawable.sprite_0, R.drawable.sprite_1,R.drawable.sprite_2,R.drawable.sprite_3,R.drawable.sprite_4, R.drawable.sprite_5,R.drawable.sprite_6,R.drawable.sprite_7)
            actions.setIcon(imagesIds[selected].toLong())
            LazyRow() {
                items(imagesIds.size){
                    if (it == selected){
                        Image(
                            painter = painterResource(imagesIds[it]),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .padding(horizontal = 6.dp)
                                .size(85.dp)
                                .selectable(
                                    selected = selected == it,
                                    onClick = {
                                        selected = it;actions.setIcon(imagesIds[it].toLong())
                                    })
                                .border(
                                    3.dp,
                                    MaterialTheme.colorScheme.secondary,
                                    RoundedCornerShape(10.dp)
                                )
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.tertiary)
                            ,
                            contentDescription = "deca",
                        )
                    }else{
                        Image(
                            painter = painterResource(imagesIds[it]),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .padding(horizontal = 5.dp)
                                .size(80.dp)
                                .selectable(
                                    selected = selected == it,
                                    onClick = {
                                        selected = it;actions.setIcon(imagesIds[it].toLong())
                                    })
                                .clip(RoundedCornerShape(20.dp)),
                            contentDescription = "deca"
                        )
                    }
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
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}