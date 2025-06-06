package com.example.grablist.ui.theme

import android.app.Activity
import android.os.Build
import android.view.View
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.example.grablist.R

private val DarkColorScheme = darkColorScheme(
    primary = BluePrimaryDark,
    onPrimary = TextPrimaryDark,

    secondary = BlueSecondaryDark,
    onSecondary = TextPrimaryDark,

    tertiary = BlueTertiaryDark,
    onTertiary = TextPrimaryDark,

    background = BackgroundDark,
    onBackground = TextPrimaryDark,

    surface = SurfaceDark,
    onSurface = TextPrimaryDark,

    error = ErrorRed,
    onError = TextPrimaryDark,

    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextTertiary,

)

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = TextPrimary,

    secondary = BlueSecondary,
    onSecondary = Color.White,

    tertiary = BlueTertiary,
    onTertiary = TextPrimary,

    background = BackgroundLight,
    onBackground = TextSecondary,

    surface = SurfaceBackground,
    onSurface = TextSecondary,

    error = ErrorRed,
    onError = Color.White,

    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = TextTertiary,


)

@Composable
fun GrabListTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {

            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }


    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            window.navigationBarColor = colorScheme.primary.toArgb()
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}