package com.example.apptest.feature_train.presentation.trainingExercises.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.apptest.feature_train.domain.model.Exercise
import com.example.apptest.feature_train.domain.model.ExerciseSet
import com.example.apptest.feature_train.domain.model.TrainingExercise
import com.example.apptest.feature_train.domain.use_case.exercise_use_case.ExerciseUseCases
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ExtendedTrainingExerciseItem(
    trainingExercise: TrainingExercise,
    sets: List<ExerciseSet>,
    exerciseUseCases: ExerciseUseCases,
    modifier: Modifier = Modifier,
    onAddSet: () -> Unit = {},
    onDeleteSet: (ExerciseSet) -> Unit = {},
    onDeleteTrainingExercise: () -> Unit = {},
) {
    val exercise by exerciseUseCases.getExerciseById(trainingExercise.exerciseId).collectAsState(initial = null)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
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
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row {
                    IconButton(onClick = onAddSet) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add set"
                        )
                    }
                    IconButton(onClick = onDeleteTrainingExercise) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete training exercise"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sets List (Horizontal Arrangement)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                sets.forEach { set ->
                    SetItem(
                        setNumber = set.setNumber ?: 1,
                        reps = set.reps ?: 0,
                        onDelete = { onDeleteSet(set) }
                    )
                }
            }
        }
    }
}