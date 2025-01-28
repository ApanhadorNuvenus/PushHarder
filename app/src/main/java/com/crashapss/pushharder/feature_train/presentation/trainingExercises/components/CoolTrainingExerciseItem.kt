package com.crashapss.pushharder.feature_train.presentation.trainingExercises.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.crashapss.pushharder.feature_train.domain.model.Exercise
import com.crashapss.pushharder.feature_train.domain.model.ExerciseSet
import com.crashapss.pushharder.feature_train.domain.model.TrainingExercise

@Composable
fun CoolTrainingExerciseItem(
    trainingExercise: TrainingExercise,
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

    val barColor = Color(0xFF4CAF50)
    val desaturatedBarColor = barColor.copy(alpha = 0.3f)
    val unfilledBarColor = Color.LightGray

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
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                exercise?.let {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f) // Exercise name takes up the available space
                    )
                }

                Spacer(modifier = Modifier.width(8.dp)) // Add some space between name and indicators


                // --- WEIGHT and FAILURE INDICATORS ---
                Column(
                    horizontalAlignment = Alignment.End // Align text to the end (right)
                ) {
                    if (trainingExercise.weights != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Weights: ",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "${trainingExercise.weights} kg",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Light),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        }
                    }

                    if (trainingExercise.failure == true) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Failure ",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Red
                            )
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Failure",
                                tint = Color.Red,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // --- PROGRESS BAR ---
            Text(
                text = if (exerciseGoal != null) "Max set vs Goal" else "",
                style = MaterialTheme.typography.bodySmall
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.weight(6f) // Progress bar takes 6/7 of the width
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
                        trackColor = Color.Transparent
                    )
                }

                Spacer(modifier = Modifier.width(8.dp)) // Space between progress bar and text

                Text(
                    text = if (exerciseGoal != null) "$maxRepsInSet/$exerciseGoal" else "$maxRepsInSet",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f) // Text takes 1/7 of the width
                )
            }

            Spacer(modifier = Modifier.height(4.dp)) // Add some space above "All: totalReps"

            Text(
                text = "All: $totalReps",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}