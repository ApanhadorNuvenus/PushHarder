package com.example.apptest.feature_train.presentation.trainingExercises.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
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
    val maxRepsInSet = sets.maxOfOrNull { it.reps ?: 0 } ?: 0
    val exerciseGoal = exercise?.goal
    val progress = if (exerciseGoal != null && exerciseGoal != 0) {
        maxRepsInSet.toFloat() / exerciseGoal.toFloat()
    } else {
        1f
    }

    // Define colors for the bar
    val barColor = Color(0xFF4CAF50) // Example: Green color
    val desaturatedBarColor = barColor.copy(alpha = 0.3f)
    val unfilledBarColor = Color.LightGray // Color for the unfilled portion

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
            }

            Spacer(modifier = Modifier.height(4.dp))

            // LinearProgressIndicator with weighted width
            Text(
                text = if (exerciseGoal != null) {
                    "Max set vs Goal"
                } else {
                    ""
                },
                style = MaterialTheme.typography.bodySmall
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Use weight to allocate 6/7 of the width to the progress bar
                Box(
                    modifier = Modifier
                        .weight(6f)
                ) {
                    LinearProgressIndicator(
                        progress = { progress.coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .drawBehind {
                                drawRect(
                                    color = unfilledBarColor,
                                    size = size,
                                    style = Fill
                                )
                            },
                        color = if (exerciseGoal != null) barColor else desaturatedBarColor,
                        trackColor = Color.Transparent // Make default track invisible
                    )
                }

                // Add Spacer here
                Spacer(modifier = Modifier.width(8.dp))

                // Use weight to allocate 1/7 of the width to the text
                Text(
                    text = if (exerciseGoal != null) {
                        "$maxRepsInSet/${exerciseGoal}"
                    } else {
                        "$maxRepsInSet"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                text = "All: $totalReps",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}