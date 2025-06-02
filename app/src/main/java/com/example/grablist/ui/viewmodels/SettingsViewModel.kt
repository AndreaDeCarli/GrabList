package com.example.grablist.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grablist.data.database.Product
import com.example.grablist.data.database.ShopList
import com.example.grablist.data.database.Theme
import com.example.grablist.data.repositories.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SettingsState(val username: String, val theme: Theme)

class SettingsViewModel(
    private val repository: SettingsRepository
) : ViewModel() {
    var state by mutableStateOf(SettingsState("", Theme.System))
        private set

    fun setUsername(username: String) {
        state = SettingsState(username, state.theme)
        viewModelScope.launch {
            repository.setUsername(username)
        }
    }

    fun setTheme(theme: Theme) {
        state = SettingsState(username = state.username, theme = theme)
        viewModelScope.launch {
            repository.setTheme(theme)
        }
    }

    init {
        viewModelScope.launch {
            state = SettingsState(repository.username.first(), Theme.valueOf(repository.theme.first()))
        }
    }
}
