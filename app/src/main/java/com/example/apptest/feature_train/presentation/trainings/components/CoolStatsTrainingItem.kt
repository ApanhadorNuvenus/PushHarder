package com.example.apptest.feature_train.presentation.stats.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.apptest.feature_train.domain.model.Training
import com.example.apptest.feature_train.domain.use_case.exercise_use_case.ExerciseUseCases
import com.example.apptest.feature_train.presentation.trainingExercises.components.CoolTrainingExerciseItem
import com.example.apptest.feature_train.presentation.trainings.TrainingsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CoolStatsTrainingItem(
    training: Training,
    selectedExerciseName: String?,
    exerciseUseCases: ExerciseUseCases,
    modifier: Modifier = Modifier,
    viewModel: TrainingsViewModel = hiltViewModel()
) {
    val trainingId = training.id
    val trainingExercisesWithSets by viewModel.trainingExercisesWithSets.collectAsState()
    val exercisesForTraining = trainingExercisesWithSets[trainingId] ?: emptyList()

    // Filter exercises based on selectedExerciseName
    val filteredExercises = if (!selectedExerciseName.isNullOrBlank()) {
        exercisesForTraining.filter { it.exercise?.name == selectedExerciseName }
    } else {
        emptyList()
    }

    // Format the date
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val formattedDate = dateFormatter.format(Date(training.date))

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = training.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Display the formatted date
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Display exercises only if filteredExercises is not empty
            if (filteredExercises.isNotEmpty()) {
                Column {
                    filteredExercises.forEach { item ->
                        CoolTrainingExerciseItem(
                            exercise = item.exercise,
                            sets = item.sets
                        )
                    }
                }
            } else {
                // Optionally, display a message when no matching exercises are found
                Text(
                    text = "No exercises for this training match the selected filter.",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}