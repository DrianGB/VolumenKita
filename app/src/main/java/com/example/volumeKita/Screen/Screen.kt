package com.example.volumeKita.Screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector


val NAVIGATION_MAIN = listOf(
    Screen.Home,
    Screen.Settings
)
sealed class Screen(
    var route: String,
    var title: String = "empty",
    var icon: ImageVector = Icons.Default.Home
){
    object Splash: Screen(
        route = "screen_splash"
    )
    object Home: Screen(
        route = "screen_home",
        title = "Home",
        icon = Icons.Default.Home
    )
    object Settings: Screen(
        route = "screen_settings",
        title = "Settings",
        icon = Icons.Default.Settings
    )
}