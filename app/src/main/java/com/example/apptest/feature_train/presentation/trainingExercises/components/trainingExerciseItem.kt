package com.example.apptest.feature_train.presentation.trainingExercises.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.apptest.feature_train.domain.model.ExerciseType
import com.example.apptest.feature_train.presentation.sets.ExerciseSetsEvent
import com.example.apptest.feature_train.presentation.sets.ExerciseSetsViewModel
import com.example.apptest.feature_train.presentation.sets.components.ExerciseSetItem

@Composable
fun TrainingExerciseItem(
    trainingExerciseId: String,
    exerciseName: String,
    exerciseType: ExerciseType,
    reps: Int?,
    duration: Int?,
    onDelete: (() -> Unit)? = null,
    viewModel: ExerciseSetsViewModel = hiltViewModel()
) {
    val exerciseSets = viewModel.getExerciseSets(teID = trainingExerciseId)
        .collectAsState(initial = emptyList())

    Column(modifier = Modifier.padding(8.dp)) {
        Text(exerciseName, style = MaterialTheme.typography.bodyLarge)
        when (exerciseType) {
            is ExerciseType.Reps -> {
                reps?.let {
                    Text("Reps: $it", style = MaterialTheme.typography.bodyMedium)
                }
            }
            is ExerciseType.Duration -> {
                duration?.let {
                    Text("Duration: $it seconds", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        exerciseSets.value.forEach { set ->
            ExerciseSetItem(sets = set.sets, reps = set.reps, onDelete = { viewModel.onEvent(
                ExerciseSetsEvent.deleteExerciseSet(set)) })
        }
        onDelete?.let { // Display delete button if onDelete is provided
            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}
