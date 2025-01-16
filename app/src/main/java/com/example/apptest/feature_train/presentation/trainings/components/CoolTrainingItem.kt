package com.example.apptest.feature_train.presentation.trainings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.apptest.feature_train.domain.model.Training
import com.example.apptest.feature_train.domain.use_case.exercise_use_case.ExerciseUseCases
import com.example.apptest.feature_train.presentation.trainingExercises.components.CoolTrainingExerciseItem
import com.example.apptest.feature_train.presentation.trainingExercises.components.TrainingExerciseList
import com.example.apptest.feature_train.presentation.trainings.TrainingsViewModel

@Composable
fun CoolTrainingItem(
    training: Training,
    navController: NavController,
    modifier: Modifier = Modifier,
    onDeleteTraining: () -> Unit,
    exerciseUseCases: ExerciseUseCases,
    viewModel: TrainingsViewModel = hiltViewModel()
) {
    val trainingId = training.id
    val trainingExercisesWithSets by viewModel.trainingExercisesWithSets.collectAsState()
    val exercisesForTraining = trainingExercisesWithSets[trainingId] ?: emptyList()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable {
                navController.navigate("add_edit_training_screen?trainingId=${training.id}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant, // Use a different color for the card
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant // Text color for contrast
                )
                IconButton(
                    onClick = onDeleteTraining,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete training",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant // Icon color for contrast
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))

            // List of CoolTrainingExerciseItems
            Column {
                exercisesForTraining.forEach { item ->
                    CoolTrainingExerciseItem(
                        exercise = item.exercise,
                        sets = item.sets
                    )
                }
            }
        }
    }
}