package com.example.grablist.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.grablist.data.database.Location
import com.example.grablist.data.database.ShopList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.maplibre.android.style.light.Position


data class AddShopListState(
    val title: String = "",
    val date: String = "",
    val iconId: Long = 0,
    val locationName: String = "",
    val longitude: Double = 0.0,
    val latitude: Double = 0.0,

    val showPermissionAlert: Boolean = false,
    val showNoGPSAlert: Boolean = false,
    val saveInCalender: Boolean = false,
    val usePosition: Boolean = false,
    val currentPosition: Location = Location()

    ) {
    val canSubmit get() = title.isNotBlank() && iconId != 0L

    fun toShopList() = ShopList(
        title = title,
        date = date,
        iconId = iconId,
        location = Location(
            name = locationName,
            longitude = longitude,
            latitude = latitude
        )
    )
}

interface AddShopListActions {
    fun setTitle(title: String)
    fun setDate(date: String)
    fun setIcon(id: Long)
    fun setLocationName(name: String)
    fun setLongitude(number: Double)
    fun setLatitude(number: Double)

    fun setShowPermissionAlert(value: Boolean)
    fun setShowNoGPSAlert(value: Boolean)
    fun setSaveInCalender(value: Boolean)
    fun setUsePosition(value: Boolean)
    fun setCurrentLocation(location: Location)
}

class AddShopListViewModel : ViewModel() {
    private val _state = MutableStateFlow(AddShopListState())
    val state = _state.asStateFlow()

    val actions = object : AddShopListActions {
        override fun setTitle(title: String) =
            _state.update { it.copy(title = title) }

        override fun setDate(date: String) =
            _state.update { it.copy(date = date) }

        override fun setIcon(id: Long) =
            _state.update { it.copy(iconId = id) }

        override fun setShowPermissionAlert(value: Boolean) =
            _state.update { it.copy(showPermissionAlert = value) }

        override fun setSaveInCalender(value: Boolean) =
            _state.update { it.copy(saveInCalender = value) }

        override fun setLocationName(name: String) =
            _state.update { it.copy(locationName = name) }

        override fun setLongitude(number: Double) =
            _state.update { it.copy(longitude = number) }

        override fun setLatitude(number: Double) =
            _state.update { it.copy(latitude = number) }

        override fun setUsePosition(value: Boolean) =
            _state.update { it.copy(usePosition = value) }

        override fun setCurrentLocation(location: Location) =
            _state.update { it.copy(currentPosition = location) }

        override fun setShowNoGPSAlert(value: Boolean) =
            _state.update { it.copy(showNoGPSAlert = value) }
    }
}