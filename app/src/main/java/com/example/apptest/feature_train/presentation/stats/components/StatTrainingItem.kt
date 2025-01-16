package com.example.apptest.feature_train.presentation.stats.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.apptest.feature_train.domain.model.Training
import com.example.apptest.feature_train.presentation.trainingExercises.TrainingExerciseWithSets
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun StatTrainingItem(
    training: Training,
    selectedExerciseName: String?,
    trainingExercisesWithSets: List<TrainingExerciseWithSets>
) {
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val formattedDate = dateFormatter.format(Date(training.date))

    val selectedExerciseRepetitions = trainingExercisesWithSets
        .firstOrNull { it.exercise?.name == selectedExerciseName }
        ?.sets
        ?.sumOf { it.reps ?: 0 } ?: 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = training.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Display total reps for the selected exercise
            if (selectedExerciseRepetitions > 0) {
                Text(
                    text = "$selectedExerciseName: $selectedExerciseRepetitions reps",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Text(
                    text = "No $selectedExerciseName in this training",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}