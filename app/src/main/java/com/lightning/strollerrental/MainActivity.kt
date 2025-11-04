package com.lightning.strollerrental

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
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

    NavHost(
        navController = navController,
        startDestination = if (uiState.showSplash) "splash" else "home"
    ) {
        composable("splash") {
            SplashScreen(
                onStart = {
                    viewModel.dismissSplash()
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(
                uiState = uiState,
                onStartRenting = {
                    viewModel.startRenting()
                    navController.navigate("renting")
                },
                onUpdateDelay = { viewModel.updateStepDelay(it) },
                onToggleForceFail = { viewModel.toggleForceFail() },
                onNavigateToSettings = { navController.navigate("settings") }
            )

            // Show error dialog if in error state
            if (uiState.currentState == RentalState.ERROR && uiState.errorMessage != null) {
                ErrorDialog(
                    message = uiState.errorMessage!!,
                    onRetry = {
                        viewModel.retryAfterError()
                    },
                    onDismiss = {
                        viewModel.retryAfterError()
                    }
                )
            }
        }

        composable("renting") {
            RentingScreen(currentStep = uiState.currentStep)

            // Navigate to rented when state changes
            if (uiState.currentState == RentalState.RENTED) {
                navController.navigate("rented") {
                    popUpTo("home") { inclusive = false }
                }
            }

            // Show error dialog and navigate back
            if (uiState.currentState == RentalState.ERROR && uiState.errorMessage != null) {
                ErrorDialog(
                    message = uiState.errorMessage!!,
                    onRetry = {
                        viewModel.retryAfterError()
                        navController.popBackStack()
                    },
                    onDismiss = {
                        viewModel.retryAfterError()
                        navController.popBackStack()
                    }
                )
            }
        }

        composable("rented") {
            RentedScreen(
                strollerId = uiState.strollerId,
                startAt = uiState.startAt,
                elapsedSec = uiState.elapsedSec,
                onStartReturning = {
                    viewModel.startReturning()
                    navController.navigate("returning")
                }
            )

            // Show error dialog if in error state
            if (uiState.currentState == RentalState.ERROR && uiState.errorMessage != null) {
                ErrorDialog(
                    message = uiState.errorMessage!!,
                    onRetry = {
                        viewModel.retryAfterError()
                    },
                    onDismiss = {
                        viewModel.retryAfterError()
                    }
                )
            }
        }

        composable("returning") {
            ReturningScreen(currentStep = uiState.currentStep)

            // Navigate to returned when state changes
            if (uiState.currentState == RentalState.RETURNED) {
                navController.navigate("returned") {
                    popUpTo("home") { inclusive = false }
                }
            }

            // Show error dialog and navigate back
            if (uiState.currentState == RentalState.ERROR && uiState.errorMessage != null) {
                ErrorDialog(
                    message = uiState.errorMessage!!,
                    onRetry = {
                        viewModel.retryAfterError()
                        navController.popBackStack()
                    },
                    onDismiss = {
                        viewModel.retryAfterError()
                        navController.popBackStack()
                    }
                )
            }
        }

        composable("returned") {
            ReturnedScreen(
                strollerId = uiState.strollerId,
                elapsedSec = uiState.elapsedSec,
                onGoHome = {
                    viewModel.reset()
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }

        composable("settings") {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
