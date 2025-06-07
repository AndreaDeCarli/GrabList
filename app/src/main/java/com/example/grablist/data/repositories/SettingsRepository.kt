package com.example.grablist.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
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
    }

    val username = dataStore.data.map { it[USERNAME_KEY] ?: "username" }
    val theme = dataStore.data.map { it[THEME_KEY] ?: "System" }
    val progress = dataStore.data.map { it[SHOPPING_PROGRESS] ?: 0 }

    suspend fun setUsername(username: String) = dataStore.edit { it[USERNAME_KEY] = username }

    suspend fun setTheme(theme: Theme) = dataStore.edit { it[THEME_KEY] = theme.toString() }

    suspend fun increaseProgress() = dataStore.edit { it[SHOPPING_PROGRESS] = progress.first() + 1 }

    suspend fun resetProgress() = dataStore.edit { it[SHOPPING_PROGRESS] = 0 }

}
