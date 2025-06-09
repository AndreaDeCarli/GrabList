package com.example.grablist.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.grablist.data.database.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AddProductState(
    val name: String = "",
    val imageUri: Uri = Uri.EMPTY,
    val favorite: Boolean = false,

    val showPermissionAlert: Boolean = false

    ) {
    val canSubmit get() = name.isNotBlank()

    fun toProduct() = Product(
        name = name,
        imageUri = imageUri.toString(),
        favorite = favorite,
    )
}

interface AddProductActions {
    fun setName(name: String)
    fun setFavorite(fav: Boolean)
    fun setImageUri(uri: Uri)

    fun setShowPermissionAlert(value: Boolean)
}

class AddNewProductViewModel : ViewModel() {
    private val _state = MutableStateFlow(AddProductState())
    val state = _state.asStateFlow()

    val actions = object : AddProductActions {
        override fun setName(name: String) =
            _state.update { it.copy(name = name) }

        override fun setFavorite(fav: Boolean) =
            _state.update { it.copy(favorite = fav) }

        override fun setImageUri(uri: Uri) =
            _state.update { it.copy(imageUri = uri) }

        override fun setShowPermissionAlert(value: Boolean) =
            _state.update { it.copy(showPermissionAlert = value) }

    }
}