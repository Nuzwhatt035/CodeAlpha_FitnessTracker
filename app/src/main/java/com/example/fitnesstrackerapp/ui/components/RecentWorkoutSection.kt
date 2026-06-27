package com.example.fitnesstrackerapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.fitnesstrackerapp.data.Roomdatabase.FitnessEntity



@Composable
fun RecentWorkoutSection(
    workouts: List<FitnessEntity>,
    onDeleteWorkout: (FitnessEntity) -> Unit,
    onEditWorkout: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "Recent Workouts",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (workouts.isEmpty()) {
            Text(
                text = "No workouts logged yet. Get moving!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        } else {
            // Iterates cleanly through stored workout entities
            workouts.forEach { workout ->
                SwipeableWorkoutItem(
                    workout = workout,
                    onDelete = { onDeleteWorkout(workout) },
                    onEdit = { onEditWorkout(workout.id) }
                )
            }
        }
    }
}

@Composable
fun SwipeableWorkoutItem(
    workout: FitnessEntity,
    onDelete: (FitnessEntity) -> Unit,
    onEdit: (Int) -> Unit
) {
    // Tracks the swipe displacement threshold to trigger dismissal/deletion
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.EndToStart || dismissValue == SwipeToDismissBoxValue.StartToEnd) {
                onDelete(workout)
                true
            } else {
                false
            }
        }
    )

    // Material 3 container providing swipe-to-dismiss gesture functionality
    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            // Red background with a delete icon exposed when swiping the item card
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Workout",
                    tint = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        },
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        // Foreground workout display block
        WorkoutItem(
            workout = workout,
            onDeleteClick = { onDelete(workout) },
            onEditClick = { onEdit(workout.id) }
        )
    }
}

@Composable
fun WorkoutItem(
    workout: FitnessEntity,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    // Dynamic icon selection logic supporting custom exercise types
    val exerciseIcon: ImageVector = when (workout.exerciseType.lowercase()) {
        "running" -> Icons.Default.DirectionsRun
        "walking" -> Icons.Default.DirectionsWalk
        "cycling" -> Icons.Default.DirectionsBike
        "swimming" -> Icons.Default.Pool
        else -> Icons.Default.Timer
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Vector Image Icon block
                Icon(
                    imageVector = exerciseIcon,
                    contentDescription = "Exercise Icon",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Column {
                    Text(
                        text = workout.exerciseType.uppercase(),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Duration: ${workout.durationMinutes} min",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Show steps or calories conditionally per workout record
                    if (workout.stepsRecord > 0) {
                        Text(
                            text = "Steps: ${workout.stepsRecord}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else if (workout.caloriesBurnedRecord > 0) {
                        Text(
                            text = "Calories: ${workout.caloriesBurnedRecord}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Trailing action buttons row for Edit and Delete
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Workout",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Workout",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}