package com.example.fitnesstrackerapp.ui.components

import android.R.attr.progress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GoalProgressSection(
    currentSteps: Int,
    goalSteps: Int
){
    //calculate percentage, capping it cleanly at 1.0f (100%)   //corceIN is a very handy built-in Kotlin function used to restrict (or clamp) a value between a minimum and a maximum limit
    val progess = (currentSteps.toFloat() / goalSteps.toFloat()).coerceIn(0f, 1f)
    val percentage = (progess * 100).toInt()
 // used It takes responsibility for clipping its contents to a specific shape (like RoundedCornerShape), painting its background with a specific color, drawing borders, and handling touch clicks.
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
            shape = RoundedCornerShape(24.dp),
           color = MaterialTheme.colorScheme.surfaceVariant,
           tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
    }
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Daily Goal Progress",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "$currentSteps/$goalSteps steps",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = "$percentage% complete",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

            //Visual Circular Gauge representing steps progress
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        progress = { progess },
                        modifier = Modifier.size(64.dp),
                        strokeWidth = 6.dp,
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surface
                    )
                }
        }
    }

// coerceIn: A math function that restricts/clamps a value between a minimum and maximum limit, preventing layouts from breaking when values exceed bounds (e.g., capping progress at 100%).
// Surface: A Material container composable responsible for clipping child elements to a specific shape, applying background colors, borders, and handling elevation.
// tonalElevation: A Material 3 depth effect that simulates shadows by subtly tinting elevated surfaces with the primary theme color, helping users distinguish layered cards from flat backgrounds.


