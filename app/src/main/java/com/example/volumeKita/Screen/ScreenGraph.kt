package com.example.volumeKita.Screen

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.volumeKita.ViewModel.MainViewModel
import kotlinx.coroutines.delay

@Composable
fun ScreenGraph(navController: NavHostController,modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ){
        val TRANSITION_SPLASH = 500
        composable(
            route = Screen.Splash.route,
            exitTransition = {
                slideOutHorizontally(animationSpec = tween(TRANSITION_SPLASH),
                    targetOffsetX = {-it})
                             },
            popExitTransition =  {
                slideOutHorizontally(animationSpec = tween(TRANSITION_SPLASH),
                    targetOffsetX = {it})
            }
        ){
            Splash(modifier = modifier)
            LaunchedEffect(Unit) {
                delay(1000)
                navController.navigate(Screen.Home.route){
                    popUpTo(Screen.Splash.route){
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        }
        composable(
            route = Screen.Home.route,
            enterTransition = {
                slideInHorizontally(animationSpec = tween(TRANSITION_SPLASH),
                    initialOffsetX = {it})
            },
            popEnterTransition ={
                slideInHorizontally(animationSpec = tween(TRANSITION_SPLASH),
                    initialOffsetX = {-it})
            }
        ){
            val viewModel: MainViewModel = hiltViewModel()
            Home(navController = navController,modifier = modifier,viewModel = viewModel)
        }
        composable(
            route = Screen.Settings.route
        ){
            Settings()
        }
    }
}