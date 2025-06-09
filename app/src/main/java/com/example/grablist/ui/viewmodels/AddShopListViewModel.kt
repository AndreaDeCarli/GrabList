package com.example.grablist.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.grablist.data.database.ShopList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class AddShopListState(
    val title: String = "",
    val date: String = "",
    val iconId: Long = 0,

    val showPermissionAlert: Boolean = false,
    val saveInCalender: Boolean = false

    ) {
    val canSubmit get() = title.isNotBlank() && iconId != 0L

    fun toShopList() = ShopList(
        title = title,
        date = date,
        iconId = iconId,
    )
}

interface AddShopListActions {
    fun setTitle(title: String)
    fun setDate(date: String)
    fun setIcon(id: Long)

    fun setShowPermissionAlert(value: Boolean)
    fun setSaveInCalender(value: Boolean)
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
    }
}