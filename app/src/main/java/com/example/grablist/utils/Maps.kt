package com.example.grablist.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.grablist.data.database.Location
import com.example.grablist.data.database.ShopList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

@Composable
fun MapComposable(
    modifier: Modifier,
    shopList: ShopList) {
    val ctx = LocalContext.current

    val position = LatLng(shopList.location.latitude,shopList.location.longitude)

    val mapView = remember {
        MapView(ctx).apply {
            getMapAsync { map ->
                map.setStyle("https://tiles.openfreemap.org/styles/liberty") {
                    map.addMarker(
                        MarkerOptions()
                            .position(position)
                            .title(shopList.location.name)
                    )
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16.0))
                }
            }
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier
    )

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        val observer = LifecycleEventObserver { source, event ->
            when (event) {
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> Unit
            }
        }

        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}




@Composable
fun LocationPickerMap(
    modifier: Modifier = Modifier,
    onLocationSelected: (LatLng) -> Unit,
    currentLocation: Location
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val mapView = remember {
        MapView(context).apply {
            getMapAsync { map ->
                map.setStyle("https://tiles.openfreemap.org/styles/liberty") { _ ->
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(currentLocation.latitude, currentLocation.longitude), 9.0))
                    map.addOnMapClickListener { latLng ->
                        map.clear()
                        map.addMarker(
                            MarkerOptions()
                                .position(latLng)
                                .title("Posizione selezionata")
                        )
                        onLocationSelected(latLng)
                        true
                    }
                }
            }
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier
    )

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

class LocationService(private val ctx: Context) {
    private val fusedLocationClient = getFusedLocationProviderClient(ctx)
    private val locationManager = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private val _coordinates = MutableStateFlow<Location?>(null)
    val coordinates = _coordinates.asStateFlow()

    suspend fun getCurrentLocation(usePreciseLocation: Boolean = false): Location? {
        val locationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!locationEnabled) throw IllegalStateException("Location is disabled")

        val permissionGranted = ContextCompat.checkSelfPermission(
            ctx,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (!permissionGranted) throw SecurityException("Location permission not granted")

        val location = withContext(Dispatchers.IO) {
            fusedLocationClient.getCurrentLocation(
                if (usePreciseLocation) Priority.PRIORITY_HIGH_ACCURACY
                else Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                CancellationTokenSource().token
            ).await()
        }

        _coordinates.value =
            if (location != null) Location("", latitude = location.latitude, longitude = location.longitude)
            else null
        return coordinates.value
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun openLocationSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        if (intent.resolveActivity(ctx.packageManager) != null) {
            ctx.startActivity(intent)
        }
    }
}