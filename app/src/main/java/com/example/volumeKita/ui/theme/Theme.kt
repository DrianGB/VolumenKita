package com.example.volumeKita.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.volumeKita.ui.theme.myPrimaryDark

private val DarkColorScheme = darkColorScheme(
    primary = myPrimaryDark,
    secondary = mySecondaryDark,
    tertiary = myAccentDark,
    background = myBackgroundDark,
    surface = mySurfaceDark,
    onPrimary = myOnPrimaryDark,
    onSecondary = myOnSecondaryDark,
    onTertiary = myOnAccentDark,
    onBackground = myOnBackgroundDark,
    onSurface = myOnSurfaceDark,
)

private val LightColorScheme = lightColorScheme(
    primary = myPrimary,
    secondary = mySecondary,
    tertiary = myAccent,
    background = myBackground,
    surface = mySurface,
    onPrimary = myOnPrimary,
    onSecondary = myOnSecondary,
    onTertiary = myOnAccent,
    onBackground = myOnBackground,
    onSurface = myOnSurface,
)

@Composable
fun VolumeChangingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}