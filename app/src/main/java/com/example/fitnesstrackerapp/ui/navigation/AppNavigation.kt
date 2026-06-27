package com.example.fitnesstrackerapp.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fitnesstrackerapp.ui.screens.AddWorkoutScreen
import com.example.fitnesstrackerapp.ui.screens.DashboardScreen
import com.example.fitnesstrackerapp.ui.screens.GoalSelectionScreen
import com.example.fitnesstrackerapp.ui.screens.ProfileSetupScreen
import com.example.fitnesstrackerapp.ui.screens.WelcomeScreen
import com.example.fitnesstrackerapp.viewmodel.FitnessViewmodel

@Composable
fun AppNavigation(viewModel: FitnessViewmodel) {
    // 1. Initialize NavController to manage routing state between composables
    val navController = rememberNavController()

    Surface(modifier = Modifier.fillMaxSize()) {
        // 2. NavHost links the NavController with a navigation graph where destinations are defined
        NavHost(
            navController = navController,
            // Defines your start destination route
            startDestination = "welcome"
        ) {

            // Route 1: Welcome / Introduction Screen
            composable("welcome") {
                WelcomeScreen(
                    onGetStartedClick = {
                        // Navigates forward to Profile Setup screen upon interaction
                        navController.navigate("profile_setup")
                    }
                )
            }

            // Route 2: Profile Setup Screen
            composable("profile_setup") {
                ProfileSetupScreen(
                    viewModel = viewModel,
                    onSaveProfileComplete = {
                        // Advances to Goal Selection once profile parameters are stored
                        navController.navigate("goal_selection")
                    },
                    onBackClick = {
                        // Pops back to the welcome screen if the user decides to retreat
                        navController.popBackStack()
                    }
                )
            }

            // Route 3: Goal Selection Screen
            composable("goal_selection") {
                GoalSelectionScreen(
                    onGoalSelected = { selectedGoal ->
                        // In a multi-screen onboarding, we could save the goal here if needed.
                        // Using navigate with popUpTo guarantees the onboarding chain cannot be re-entered via back button.
                        navController.navigate("dashboard") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    },
                    onBackClick = {
                        // Allows backtracking to modify filled profile inputs
                        navController.popBackStack()
                    }
                )
            }

            // Route 4: Main Dashboard / Summary Screen
            composable("dashboard") {
                DashboardScreen(
                    viewModel = viewModel,
                    onAddWorkoutClick = {
                        // Navigates to Add Workout screen
                        navController.navigate("add_workout/-1")
                    },
                    onEditWorkoutClick = { workoutId ->
                        // Passes specific workout ID argument to pre-populate form for editing
                        navController.navigate("add_workout/$workoutId")
                    },
                    onEditProfileClick = {
                        // Routes user back to the profile setup screen for metric modifications
                        navController.navigate("profile_setup")
                    }
                )
            }

            // Route 5: Add / Edit Workout Screen (Accepts an integer argument for workoutId)
            composable(
                route = "add_workout/{workoutId}",
                arguments = listOf(navArgument("workoutId") { type = NavType.IntType })
            ) { backStackEntry ->
                val workoutId = backStackEntry.arguments?.getInt("workoutId")
                AddWorkoutScreen(
                    viewModel = viewModel,
                    onWorkoutSaved = {
                        // Navigates backward to the Dashboard
                        navController.popBackStack()
                    },
                    workoutId = workoutId
                )
            }
        }
    }
}

