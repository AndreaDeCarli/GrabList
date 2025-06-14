package com.example.grablist.data.repositories

import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.grablist.data.database.Theme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


class SettingsRepository(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val THEME_KEY = stringPreferencesKey("theme")
        private val SHOPPING_PROGRESS = intPreferencesKey("progress")
        private val PROGRESS_DATE = longPreferencesKey("progressDate")
        private val PROFILE_PIC_URI = stringPreferencesKey("profilePic")
    }

    val username = dataStore.data.map { it[USERNAME_KEY] ?: "username" }
    val theme = dataStore.data.map { it[THEME_KEY] ?: "System" }
    val progress = dataStore.data.map { it[SHOPPING_PROGRESS] ?: 0 }
    val latestProgressDate = dataStore.data.map { it[PROGRESS_DATE] ?: 0L }
    val profilePicUri = dataStore.data.map { it[PROFILE_PIC_URI] ?: Uri.EMPTY.toString() }

    suspend fun setProfilePicUri(uri: Uri) = dataStore.edit { it[PROFILE_PIC_URI] = uri.toString() }

    suspend fun setUsername(username: String) = dataStore.edit { it[USERNAME_KEY] = username }

    suspend fun setTheme(theme: Theme) = dataStore.edit { it[THEME_KEY] = theme.toString() }

    suspend fun increaseProgress() = dataStore.edit { it[SHOPPING_PROGRESS] = progress.first() + 1 }

    suspend fun resetProgress() = dataStore.edit { it[SHOPPING_PROGRESS] = 0 }

    suspend fun setLatestProgressDate(date: Long) = dataStore.edit { it[PROGRESS_DATE] = date }

}
