package com.example.volumeKita.ui.theme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.volumeKita.Screen.BottomBarMain
import com.example.volumeKita.Screen.NAVIGATION_MAIN
import com.example.volumeKita.Screen.ScreenGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val backStackNavController = navController.currentBackStackEntryAsState()
            val currentDestinationController = backStackNavController.value?.destination?.route ?: ""
            VolumeChangingTheme {
                Scaffold(
                    modifier = Modifier.Companion.fillMaxSize(),
                    bottomBar = {
                        if (NAVIGATION_MAIN.map { target -> target.route }
                                .contains(currentDestinationController)) {
                            BottomBarMain(
                                navHostController = navController
                            )
                        }
                    }
                ) { innerPadding ->
                    ScreenGraph(
                        navController = navController,
                        modifier = Modifier.Companion.padding(innerPadding)
                    )
                }
            }
        }
    }
}