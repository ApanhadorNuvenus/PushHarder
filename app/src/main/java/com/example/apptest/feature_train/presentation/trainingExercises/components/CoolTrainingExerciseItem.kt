package com.example.apptest.feature_train.presentation.trainingExercises.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.apptest.feature_train.domain.model.Exercise
import com.example.apptest.feature_train.domain.model.ExerciseSet

@Composable
fun CoolTrainingExerciseItem(
    exercise: Exercise?,
    sets: List<ExerciseSet>,
    modifier: Modifier = Modifier
) {
    val totalReps = sets.sumOf { it.reps ?: 0 }
    val maxReps = 250 // Maximum reps for any exercise
    val progress = totalReps.coerceAtMost(maxReps) / maxReps.toFloat() // Progress value (0.0 to 1.0)

    // Define a color for the bar (you can customize this)
    val barColor = Color(0xFF4CAF50) // Example: Green color

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                exercise?.let {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                }
                Text(
                    text = "$totalReps reps",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(4.dp))

            // LinearProgressIndicator
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth(),
                color = barColor
            )
        }
    }
}