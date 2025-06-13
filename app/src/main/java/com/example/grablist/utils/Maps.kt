package com.example.grablist.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.grablist.data.database.ShopList
import org.maplibre.android.MapLibre
import org.maplibre.android.annotations.MarkerOptions
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView

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
    onLocationSelected: (LatLng) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val mapView = remember {
        MapView(context).apply {
            getMapAsync { map ->
                map.setStyle("https://tiles.openfreemap.org/styles/liberty") { _ ->
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(41.9028, 12.4964), 5.0)) // Roma
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
