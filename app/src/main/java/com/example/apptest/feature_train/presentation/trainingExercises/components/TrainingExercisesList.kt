package com.example.apptest.feature_train.presentation.trainingExercises.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.DateFormat
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.apptest.feature_train.domain.model.ExerciseType
import com.example.apptest.feature_train.domain.model.Training
import com.example.apptest.feature_train.domain.model.TrainingExercise
import com.example.apptest.feature_train.presentation.trainingExercises.TrainingExercisesViewModel
import kotlinx.coroutines.flow.collect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import com.example.apptest.feature_train.presentation.trainingExercises.TrainingExercisesEvent

@Composable
fun TrainingExercisesList(
    trainingId: String,
    viewModel: TrainingExercisesViewModel = hiltViewModel()
) {
    // Use collectAsState() instead of manual collection
    val exercises = viewModel.getTrainingExercises(trainingId)
        .collectAsState(initial = emptyList())

    LazyColumn {
        items(exercises.value) { trainingExercise ->
            val exercise by viewModel.getExercise(trainingExercise.exerciseId)
                .collectAsState(initial = null)

            exercise?.let { ex ->
                TrainingExerciseItem(
                    trainingExerciseId = trainingExercise.id,
                    exerciseName = ex.name,
                    exerciseType = ex.exerciseType,
                    reps = trainingExercise.reps,
                    duration = trainingExercise.duration,
                    onDelete = {
                        viewModel.onEvent(TrainingExercisesEvent.DeleteTrainingExercise(trainingExercise))
                    }
                )
            }
        }
    }
}