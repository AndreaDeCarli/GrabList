package com.example.grablist.ui.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.grablist.data.database.Location
import com.example.grablist.ui.composables.GenericAlertDialog
import com.example.grablist.ui.composables.MainTopAppBar
import com.example.grablist.ui.viewmodels.AddShopListActions
import com.example.grablist.ui.viewmodels.AddShopListState
import com.example.grablist.utils.LocationPickerMap
import com.example.grablist.utils.LocationService
import com.example.grablist.utils.OSMDataSource
import com.example.grablist.utils.PermissionStatus
import com.example.grablist.utils.addCalendarEvent
import com.example.grablist.utils.getPrimaryCalendarId
import com.example.grablist.utils.rememberMultiplePermissions
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AddNewList (state: AddShopListState, actions: AddShopListActions, onSubmit: () -> Unit, navController: NavController){

    val ctx = LocalContext.current
    var showMap by remember { mutableStateOf(false) }
    val osmDataSource = koinInject<OSMDataSource>()
    val locationService = remember { LocationService(ctx) }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    fun setNameFromLocation(location: Location) = scope.launch {
        val place = osmDataSource.getPlace(location).displayName.split(",")
        val realName = "${place[0]},${place[2]}"
        actions.setLocationName(realName)
    }


    val calendarPermission = rememberMultiplePermissions(
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

    val positionPermission = rememberMultiplePermissions(
        listOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    ) { statuses ->
        when {
            statuses.any { it.value == PermissionStatus.Granted } -> {
                actions.setUsePosition(true)
            }
            statuses.all { it.value == PermissionStatus.PermanentlyDenied } ->
                actions.setShowPermissionAlert(true)
        }
    }

    fun getCurrentLocation() = scope.launch {
        if (positionPermission.statuses.none { it.value.isGranted }){
            positionPermission.launchPermissionRequest()
            return@launch
        }
        val tmp = try {
            locationService.getCurrentLocation() ?: return@launch
        }catch (_:IllegalStateException){
            actions.setShowNoGPSAlert(true)
            return@launch
        }
        actions.setCurrentLocation(tmp)
        showMap = true
    }

    val buttonColors = ButtonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = MaterialTheme.colorScheme.inverseSurface,
        disabledContentColor = MaterialTheme.colorScheme.inverseOnSurface
    )

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

        var showDatePicker by remember { mutableStateOf(false) }
        var selected by remember { mutableIntStateOf(0) }
        val imagesIds = listOf(R.drawable.sprite0, R.drawable.sprite1,R.drawable.sprite2,R.drawable.sprite3,R.drawable.sprite4, R.drawable.sprite5,R.drawable.sprite6,R.drawable.sprite7)

        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            item {
                OutlinedTextField(
                    onValueChange = actions::setTitle,
                    value = state.title,
                    label = { Text(stringResource(id = R.string.title_generic)) },
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                )
            }
            item {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically){
                    Button(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 15.dp),
                        onClick = {
                            showDatePicker = true
                        },
                        colors = buttonColors
                    ) {
                        Text(stringResource(R.string.select_date))
                        Icon(Icons.Filled.CalendarMonth, "Calendar")
                    }
                    Text(text = state.date, modifier = Modifier.padding(horizontal = 10.dp, vertical = 15.dp),)
                }
            }
            item {
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
                                calendarPermission.launchPermissionRequest()
                            }else{
                                actions.setSaveInCalender(false)
                            }
                        }
                    )
                    Text(stringResource(R.string.add_to_calendar))
                }
            }
            item {
                if (showDatePicker){
                    DatePickerModal(
                        onDismiss = { showDatePicker = false },
                        onDateSelected = { millis ->
                            actions.setDate(convertMillisToDate(requireNotNull(millis)))
                            showDatePicker = false})
                }
            }
            item {
                actions.setIcon(imagesIds[selected].toLong())
                LazyHorizontalGrid(
                    modifier = Modifier
                        .height(200.dp)
                        .padding(5.dp)
                        .fillMaxWidth(),
                    rows = GridCells.Fixed(2),
                ) {
                    items(imagesIds.size){
                        if (it == selected){
                            Image(
                                painter = painterResource(imagesIds[it]),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .padding(6.dp)
                                    .size(80.dp)
                                    .selectable(
                                        selected = selected == it,
                                        onClick = {
                                            selected = it;actions.setIcon(imagesIds[it].toLong())
                                        })
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.tertiary)
                                    .padding(5.dp)
                                ,
                                contentDescription = "ShoppingIcon",
                            )
                        }else{
                            Image(
                                painter = painterResource(imagesIds[it]),
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .padding(6.dp)
                                    .size(80.dp)
                                    .selectable(
                                        selected = selected == it,
                                        onClick = {
                                            selected = it;actions.setIcon(imagesIds[it].toLong())
                                        })
                                    .clip(RoundedCornerShape(20.dp)),
                                contentDescription = "ShoppingIcon"
                            )
                        }
                    }
                }
            }
            item {
                Button(
                    enabled = !showMap,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 15.dp),
                    onClick = {
                        scope.launch {
                            isLoading = true
                            getCurrentLocation().join()
                            isLoading = false
                        }
                    },
                    colors = buttonColors
                ) {
                    Text(text = stringResource(R.string.add_location))
                    if (isLoading){
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(25.dp)
                                .padding(5.dp),
                            color = MaterialTheme.colorScheme.onSecondary,
                            strokeWidth = 2.dp)
                    }else{
                        Icon(Icons.Outlined.Map, "map")
                    }
                }
            }
            item {
                if (showMap){
                    LocationPickerMap(
                        modifier = Modifier
                            .height(400.dp)
                            .padding(12.dp)
                            .fillMaxWidth(),
                        onLocationSelected = {
                            actions.setLatitude(it.latitude)
                            actions.setLongitude(it.longitude)
                            setNameFromLocation(Location("", it.longitude, it.latitude))
                        },
                        currentLocation = state.currentPosition
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(150.dp)) }
        }
        if (state.showNoGPSAlert){
            GenericAlertDialog(
                title = stringResource(R.string.no_gps_title),
                text = stringResource(R.string.no_gps_text),
                confirmText = stringResource(R.string.confirm),
                confirmAction = {
                    locationService.openLocationSettings()
                    actions.setShowNoGPSAlert(false) },
                dismissText = stringResource(R.string.dismiss),
                dismissAction = { actions.setShowNoGPSAlert(false) },
                onDismissRequest = { actions.setShowNoGPSAlert(false) },
                icon = Icons.Filled.LocationOff
            )
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