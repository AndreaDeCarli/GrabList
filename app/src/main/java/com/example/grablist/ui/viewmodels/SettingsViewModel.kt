package com.example.grablist.ui.viewmodels

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grablist.data.database.Product
import com.example.grablist.data.database.ShopList
import com.example.grablist.data.database.Theme
import com.example.grablist.data.repositories.SettingsRepository
import com.example.grablist.utils.CalendarVariables.Companion.oneHour
import com.example.grablist.utils.CalendarVariables.Companion.oneWeek
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

data class SettingsState(
    val username: String,
    val theme: Theme,
    val progress: Int,
    val latestProgressDate: Long,
    val profilePicUri: Uri
)

class SettingsViewModel(
    private val repository: SettingsRepository
) : ViewModel() {
    var state by mutableStateOf(SettingsState("", Theme.System, 0, 0, Uri.EMPTY, ))
        private set

    fun setUsername(username: String) {
        state = state.copy(username = username)
        viewModelScope.launch {
            repository.setUsername(username)
        }
    }

    fun setTheme(theme: Theme) {
        state = state.copy(theme = theme)
        viewModelScope.launch {
            repository.setTheme(theme)
        }
    }

    fun increaseProgress() {
        state = state.copy(progress = state.progress+1)
        viewModelScope.launch {
            repository.increaseProgress()
        }
    }

    fun resetProgress() {
        state = state.copy(progress = 0)
        viewModelScope.launch {
            repository.resetProgress()
        }
    }

    fun setProfilePicUri(uri: Uri) {
        state = state.copy(profilePicUri = uri)
        viewModelScope.launch {
            repository.setProfilePicUri(uri)
        }
    }

    fun setLatestProgressDate(){
        val date = System.currentTimeMillis()
        state = state.copy(latestProgressDate = date)
        viewModelScope.launch {
            repository.setLatestProgressDate(date)
        }
    }

    init {
        viewModelScope.launch {
            val progress: Int
            if (System.currentTimeMillis() - repository.latestProgressDate.first() > oneWeek){
                resetProgress()
                progress = 0
            }else{
                progress = repository.progress.first()
            }
            state = SettingsState(
                username = repository.username.first(),
                theme = Theme.valueOf(repository.theme.first().toString()),
                progress = progress,
                latestProgressDate = repository.latestProgressDate.first(),
                profilePicUri = Uri.parse(repository.profilePicUri.first().toString()))
        }
    }
}
