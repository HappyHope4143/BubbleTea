package com.happyhope.bubbletea.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Dark Color Scheme - Bubble Tea themed
private val DarkColorScheme = darkColorScheme(
    primary = BubbleTeaPrimary80,
    secondary = BubbleTeaSecondary80,
    tertiary = BubbleTeaTertiary80,
    background = BubbleTeaBackgroundDark,
    surface = BubbleTeaSurfaceDark,
    onPrimary = BubbleTeaOnPrimaryDark,
    onSecondary = BubbleTeaOnSecondaryDark,
    onBackground = BubbleTeaOnBackgroundDark,
    onSurface = BubbleTeaOnSurfaceDark,
    error = BubbleTeaError
)

// Light Color Scheme - Bubble Tea themed
private val LightColorScheme = lightColorScheme(
    primary = BubbleTeaPrimary40,
    secondary = BubbleTeaSecondary40,
    tertiary = BubbleTeaTertiary40,
    background = BubbleTeaBackground,
    surface = BubbleTeaSurface,
    onPrimary = BubbleTeaOnPrimary,
    onSecondary = BubbleTeaOnSecondary,
    onBackground = BubbleTeaOnBackground,
    onSurface = BubbleTeaOnSurface,
    error = BubbleTeaError
)

@Composable
fun BubbleTeaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled to maintain brand consistency
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
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
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}