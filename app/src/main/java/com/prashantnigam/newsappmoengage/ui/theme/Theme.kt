package com.prashantnigam.newsappmoengage.ui.theme

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Dark color scheme for the News App.
 */
private val DarkColorScheme = darkColorScheme(
    surface = Color(0xFF121212), // Surface color for dark theme
)

/**
 * Light color scheme for the News App.
 */
private val LightColorScheme = lightColorScheme() // Default light color scheme

/**
 * Composable function for the News App theme.
 *
 * @param isDarkTheme Flag to indicate whether the theme should be dark or light.
 * @param content The content of the app.
 */
@Composable
fun NewsAppMoengageTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Determine color scheme based on the dark mode preference
    val colorScheme = if (!isDarkTheme) {
        LightColorScheme
    } else {
        DarkColorScheme
    }

    // Apply system UI visibility settings
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            // Set appearance of status and navigation bars based on theme
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !isDarkTheme
        }
    }

    // Apply MaterialTheme with custom color scheme and typography
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
    ) {
        // Apply background color to the entire app
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            content()
        }
    }
}
