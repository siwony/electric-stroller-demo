package com.lightning.strollerrental

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StrollerRentalApp()
                }
            }
        }
    }
}

@Composable
fun StrollerRentalApp() {
    val navController = rememberNavController()
    val viewModel: StrollerViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    // Navigation based on state
    LaunchedEffect(uiState.currentState) {
        when (uiState.currentState) {
            RentalState.IDLE -> navController.navigate("home") {
                popUpTo("home") { inclusive = true }
            }
            RentalState.RENTING -> navController.navigate("renting") {
                popUpTo("home") { inclusive = true }
            }
            RentalState.RENTED -> navController.navigate("rented") {
                popUpTo("renting") { inclusive = true }
            }
            RentalState.RETURNING -> navController.navigate("returning") {
                popUpTo("rented") { inclusive = true }
            }
            RentalState.RETURNED -> {
                viewModel.reset()
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                uiState = uiState,
                onStartRenting = { viewModel.startRenting() },
                onUpdateDelay = { viewModel.updateStepDelay(it) },
                onToggleForceFail = { viewModel.toggleForceFail() }
            )
        }

        composable("renting") {
            RentingScreen(currentStep = uiState.currentStep)
        }

        composable("rented") {
            RentedScreen(
                onStartReturning = { viewModel.startReturning() }
            )
        }

        composable("returning") {
            ReturningScreen()
        }
    }
}
