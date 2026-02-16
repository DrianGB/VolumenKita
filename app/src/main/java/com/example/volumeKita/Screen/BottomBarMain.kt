package com.example.volumeKita.Screen

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBarMain(navHostController: NavHostController) {
    val backStackNav = navHostController.currentBackStackEntryAsState()
    val destinationCurrent = backStackNav.value?.destination
    NavigationBar{
        NAVIGATION_MAIN.forEach { screen ->
            addItemMain(
                screen = screen,
                currentDestination = destinationCurrent?.route ?: "",
                navController = navHostController
            )
        }
    }
}

@Composable
fun RowScope.addItemMain(
    screen: Screen,
    currentDestination: String,
    navController: NavHostController
){
    NavigationBarItem(
        selected = screen.route == currentDestination,
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = null
            )
        },
        label = {
            Text(
                text = screen.title
            )
        },
        onClick = {
            navController.navigate(
                screen.route
            ){
                popUpTo(Screen.Home.route){
                    inclusive = false
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    )
}