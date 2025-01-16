package com.example.apptest.feature_train.presentation.trainingExercises.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.apptest.feature_train.domain.model.Exercise
import com.example.apptest.feature_train.domain.model.ExerciseSet
import com.example.apptest.feature_train.domain.model.TrainingExercise
import com.example.apptest.feature_train.domain.use_case.exercise_use_case.ExerciseUseCases

@Composable
fun TrainingExerciseItem(
    trainingExercise: TrainingExercise,
    sets: List<ExerciseSet>,
    exercise: Exercise?,
    exerciseUseCases: ExerciseUseCases,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp), // Reduced card padding
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp) // Reduced elevation
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp) // Reduced column padding
                .fillMaxWidth()
        ) {
            // Training Exercise Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                exercise?.let {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.bodyLarge, // Smaller font
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp)) // Reduced spacing

            // Sets List
            Column {
                sets.forEach { set ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp), // Reduced padding
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Set ${set.setNumber}: ${set.reps} reps", // More concise
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}