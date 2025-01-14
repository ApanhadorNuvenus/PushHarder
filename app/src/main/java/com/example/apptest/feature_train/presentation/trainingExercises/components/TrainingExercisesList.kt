package com.example.apptest.feature_train.presentation.trainingExercises.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.apptest.feature_train.domain.model.Exercise
import com.example.apptest.feature_train.domain.model.ExerciseSet
import com.example.apptest.feature_train.presentation.exerciseSets.ExerciseSetsViewModel
import com.example.apptest.feature_train.presentation.trainingExercises.TrainingExercisesViewModel

@Composable
fun TrainingExercisesList(
    trainingId: String,
    exerciseSetViewModel: ExerciseSetsViewModel = hiltViewModel(),
    viewModel: TrainingExercisesViewModel = hiltViewModel()
) {
    val exercises by viewModel.getTrainingExercises(trainingId)
        .collectAsState(initial = emptyList())

    // Fetch sets for all exercises at once
    LaunchedEffect(exercises) { // Key by exercises so it re-triggers if exercises change
        exercises.forEach { trainingExercise ->
            exerciseSetViewModel.loadSets(trainingExercise.id)
        }
    }

    LazyColumn {
        items(exercises) { trainingExercise ->
            // Fetch the Exercise for each TrainingExercise
            val exercise by viewModel.getExercise(trainingExercise.exerciseId)
                .collectAsState(initial = null)

            // Get the sets for this specific TrainingExercise
            val sets by exerciseSetViewModel.getSetsForExercise(trainingExercise.id)
                .collectAsState(initial = emptyList())

            exercise?.let { ex ->
                TrainingExerciseItem(
                    exercise = ex,
                    sets = sets
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}