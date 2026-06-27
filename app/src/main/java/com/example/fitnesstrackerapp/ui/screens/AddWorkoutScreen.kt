package com.example.fitnesstrackerapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.fitnesstrackerapp.data.Roomdatabase.FitnessEntity
import com.example.fitnesstrackerapp.viewmodel.FitnessViewmodel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWorkoutScreen(
    viewModel: FitnessViewmodel,
    onWorkoutSaved: () -> Unit, // Higher-order lambda to navigate backward
    workoutId: Int? // ID used to determine if we are adding a new workout or editing
) {
    // Access Android context locally for Toast messages
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // State variables holding form input values
    var exerciseType by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var steps by remember { mutableStateOf("") }

    // Dropdown visibility state
    var isExposedDropdownExpanded by remember { mutableStateOf(false) }
    val exerciseOptions = listOf("Running", "Walking", "Cycling", "Weightlifting")

    // Input error trackers
    var exerciseError by remember { mutableStateOf(false) }
    var durationError by remember { mutableStateOf(false) }

    // LaunchedEffect executes when screen loads or when workout changes, fetching DB records for editing
    LaunchedEffect(workoutId) {
        if (workoutId != null && workoutId != -1) {
            val existingWorkout = viewModel.getRecordById(workoutId)
            existingWorkout?.let { workout ->
                exerciseType = workout.exerciseType
                duration = workout.durationMinutes.toString()
                calories = workout.caloriesBurnedRecord.toString()
                steps = workout.stepsRecord.toString()
            }
        }
    }

    // Scaffold holding top app bar and body content
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (workoutId == null || workoutId == -1) "ADD WORKOUT" else "EDIT WORKOUT",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onWorkoutSaved() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to Dashboard"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        // Wrapped Column with verticalScroll to support smaller screens
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                // Neumorphic styling block: Elevated extruded soft shadow with subtle outline border
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                )
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(24.dp)
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(24.dp)
                )
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Exposed dropdown container letting users either pick options or type freely
            ExposedDropdownMenuBox(
                expanded = isExposedDropdownExpanded,
                onExpandedChange = { isExposedDropdownExpanded = !isExposedDropdownExpanded }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    value = exerciseType,
                    onValueChange = {
                        exerciseType = it
                        if (it.isNotBlank()) exerciseError = false
                    },
                    label = { Text(text = "Exercise Type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExposedDropdownExpanded) },
                    isError = exerciseError,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    ),
                    supportingText = {
                        if (exerciseError) {
                            Text(text = "Exercise type cannot be empty", color = MaterialTheme.colorScheme.error)
                        }
                    }
                )

                ExposedDropdownMenu(
                    expanded = isExposedDropdownExpanded,
                    onDismissRequest = { isExposedDropdownExpanded = false }
                ) {
                    exerciseOptions.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                exerciseType = selectionOption
                                isExposedDropdownExpanded = false
                                exerciseError = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = duration,
                onValueChange = {
                    duration = it
                    if (it.isNotEmpty() && (it.toIntOrNull() ?: 0) > 0) {
                        durationError = false
                    }
                },
                label = { Text(text = "Duration (mins)") },
                isError = durationError,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                ),
                supportingText = {
                    if (durationError) {
                        Text(text = "Duration must be greater than 0", color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Conditional field showing calories only for cycling and walking/weightlifting
            if (exerciseType.contains("Cycling", ignoreCase = true) ||
                exerciseType.contains("Weightlifting", ignoreCase = true)) {
                OutlinedTextField(
                    value = calories,
                    onValueChange = { calories = it },
                    label = { Text(text = "Calories Burned") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Conditional field showing steps only for walking and running
            if (exerciseType.contains("Running", ignoreCase = true) ||
                exerciseType.contains("Walking", ignoreCase = true)) {
                OutlinedTextField(
                    value = steps,
                    onValueChange = { steps = it },
                    label = { Text(text = "Steps") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Safe parsing of user string input to integer values
                    val parsedDuration = duration.toIntOrNull() ?: 0
                    val parsedCalories = calories.toIntOrNull() ?: 0
                    val parsedSteps = steps.toIntOrNull() ?: 0

                    if (exerciseType.isBlank()) {
                        exerciseError = true
                    }
                    if (parsedDuration <= 0) {
                        durationError = true
                    }

                    // Proceed only when both validation flags are clear
                    if (!exerciseError && !durationError) {
                        // Securely pass ID: if editing, use workoutId; if adding new, set to 0
                        // (autoGenerate = true translates 0 into an incremented unique key)
                        val safeId = if (workoutId != null && workoutId != -1) workoutId else 0

                        val workout = FitnessEntity(
                            id = safeId,
                            date = "today", // Hardcoded safely as requested by your model schema
                            exerciseType = exerciseType.trim(),
                            durationMinutes = parsedDuration,
                            caloriesBurnedRecord = parsedCalories,
                            stepsRecord = parsedSteps
                        )

                        coroutineScope.launch(Dispatchers.Main) {
                            if (workoutId == null || workoutId == -1) {
                                viewModel.insertRecord(workout)
                            } else {
                                viewModel.updateRecord(workout)
                            }
                            Toast.makeText(context, "Workout Saved Successfully!", Toast.LENGTH_SHORT).show()
                            onWorkoutSaved()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = "SAVE",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }
        }
    }
}