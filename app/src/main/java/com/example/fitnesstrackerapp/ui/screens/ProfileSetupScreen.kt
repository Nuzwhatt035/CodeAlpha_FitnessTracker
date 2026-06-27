package com.example.fitnesstrackerapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fitnesstrackerapp.viewmodel.FitnessViewmodel

/**
 * Screen capturing personal profile information (Name, Age, Height, Weight, Gender).
 * Added explicit onBackClick parameter to allow Backward Navigation flow.
 */
@Composable
fun ProfileSetupScreen(
    viewModel: FitnessViewmodel,
    onSaveProfileComplete: () -> Unit,
    onBackClick: () -> Unit // Backward navigation handler
) {
    // Load existing profile from the DB when the screen opens
    LaunchedEffect(Unit) {
        viewModel.loadUserProfile()
    }

    // Collect profile state if an edit is happening
    val existingProfile by viewModel.userProfile.collectAsState()

    var name by remember(existingProfile) { mutableStateOf(existingProfile?.fullName ?: "") }
    var age by remember(existingProfile) { mutableStateOf(existingProfile?.age?.toString() ?: "") }
    var height by remember(existingProfile) { mutableStateOf(existingProfile?.heightCm?.toString() ?: "") }
    var weight by remember(existingProfile) { mutableStateOf(existingProfile?.weightKg?.toString() ?: "") }

    val genderOptions = listOf("Male", "Female", "Other")
    var selectedGender by remember(existingProfile) {
        mutableStateOf(existingProfile?.gender ?: genderOptions[0])
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back navigation header button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Go Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Text(
            text = "Setup Your Profile",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Enter your details to personalize your fitness journey.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = height,
            onValueChange = { height = it },
            label = { Text("Height (cm)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight (kg)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Gender",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            genderOptions.forEach { gender ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    RadioButton(
                        selected = (selectedGender == gender),
                        onClick = { selectedGender = gender }
                    )
                    Text(text = gender, modifier = Modifier.padding(start = 4.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                val parsedAge = age.toIntOrNull() ?: 0
                val parsedHeight = height.toFloatOrNull() ?: 0f
                val parsedWeight = weight.toFloatNull() ?: 0f

                if (name.isNotBlank() && parsedAge > 0 && parsedHeight > 0f && parsedWeight > 0f) {
                    viewModel.saveProfile(
                        name = name,
                        age = parsedAge,
                        height = parsedHeight,
                        weight = parsedWeight,
                        gender = selectedGender,
                        goal = existingProfile?.fitnessGoal ?: "Maintain Fitness"
                    )
                    onSaveProfileComplete()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = "Continue",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// Extension function to parse Floats safely
fun String.toFloatNull(): Float? {
    return this.toFloatOrNull() ?: this.toIntOrNull()?.toFloat()
}

