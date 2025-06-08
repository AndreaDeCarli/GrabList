package com.example.grablist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.grablist.data.database.Theme
import com.example.grablist.ui.GrabListNavGraph
import com.example.grablist.ui.theme.GrabListTheme
import com.example.grablist.ui.viewmodels.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val settingsViewModel = koinViewModel<SettingsViewModel>()
            val settingsState = settingsViewModel.state

            GrabListTheme(
                darkTheme = when (settingsState.theme) {
                    Theme.Dark -> true
                    Theme.Light -> false
                    else -> isSystemInDarkTheme()
                },
                dynamicColor = false
            ) {
                val navController = rememberNavController()
                GrabListNavGraph(navController, settingsViewModel, settingsState)
            }
        }
    }
}
