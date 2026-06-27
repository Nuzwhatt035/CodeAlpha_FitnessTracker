package com.example.fitnesstrackerapp.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fitnesstrackerapp.ui.components.FitnessFab
import com.example.fitnesstrackerapp.ui.components.GoalProgressSection
import com.example.fitnesstrackerapp.ui.components.RecentWorkoutSection
import com.example.fitnesstrackerapp.ui.components.SummarySection
import com.example.fitnesstrackerapp.viewmodel.FitnessViewmodel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: FitnessViewmodel,
    onAddWorkoutClick: () -> Unit,
    onEditWorkoutClick: (Int) -> Unit,
    onEditProfileClick: () -> Unit // Callback navigating back to Setup Profile
) {
    // Collect persistent room DB records and profile state
    val workouts by viewModel.records.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    // Retrieve active profile configurations or defaults
    val displayName = userProfile?.fullName ?: "Fitness Enthusiast"
    val weight = userProfile?.weightKg ?: 70f
    val height = userProfile?.heightCm ?: 175f

    // Neumorphic BMI calculation engine
    val heightM = height / 100f
    val bmi = if (heightM > 0f) weight / (heightM * heightM) else 0f
    val bmiCategory = when {
        bmi < 18.5 -> "Underweight"
        bmi < 24.9 -> "Normal weight"
        bmi < 29.9 -> "Overweight"
        else -> "Obesity"
    }

    // Guarantee records reload cleanly when Dashboard appears
    LaunchedEffect(Unit) {
        viewModel.loadRecords()
        viewModel.loadUserProfile()
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // BackHandler intercepts the back button on the Dashboard, routing back to Profile Setup
    BackHandler(enabled = true) {
        onEditProfileClick()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Good Morning, $displayName") },
                actions = {
                    IconButton(
                        onClick = { viewModel.toggleTheme() },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Brightness4,
                            contentDescription = "Toggle Theme"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FitnessFab(
                onAddWorkoutClick = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Navigating to Add Workout")
                    }
                    onAddWorkoutClick()
                }
            )
        }
    ) { innerPadding ->
        // Standard LazyColumn properly displaying all listed modules without truncation
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Extruded Neumorphic Summary Card
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .shadow(elevation = 6.dp, shape = RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(24.dp))
                        .border(
                            BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Body Mass Index (BMI)",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "%.1f".format(bmi),
                                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Category: $bmiCategory",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Button(
                            onClick = onEditProfileClick,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = "Edit Profile", fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Weight", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = "${weight.toInt()} kg", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Height", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = "${height.toInt()} cm", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Gender", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = userProfile?.gender ?: "N/A", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Clear All Button row
            item {
                Button(
                    onClick = {
                        viewModel.clearAllWorkouts()
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("All workouts cleared successfully")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Clear All Workouts")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Workout summary data calculations
            val totalSteps = workouts.sumOf { it.stepsRecord }
            val totalCalories = workouts.sumOf { it.caloriesBurnedRecord }
            val totalDuration = workouts.sumOf { it.durationMinutes }

            // Displays dynamic workout summary totals
            item {
                SummarySection(
                    steps = totalSteps,
                    calories = totalCalories,
                    duration = totalDuration
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Goal progress gauge dial section
            item {
                GoalProgressSection(
                    currentSteps = totalSteps,
                    goalSteps = 10000
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Populated recent workout cards
            item {
                RecentWorkoutSection(
                    workouts = workouts,
                    onDeleteWorkout = { workoutEntity ->
                        viewModel.deleteRecord(workoutEntity)
                    },
                    onEditWorkout = { workoutId ->
                        onEditWorkoutClick(workoutId)
                    }
                )
            }
        }
    }
}