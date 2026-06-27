package com.example.fitnesstrackerapp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton

@Composable
fun FitnessFab(
    onAddWorkoutClick: ()  -> Unit

){

    FloatingActionButton(
        onClick = onAddWorkoutClick
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Workout"
        )
    }

}