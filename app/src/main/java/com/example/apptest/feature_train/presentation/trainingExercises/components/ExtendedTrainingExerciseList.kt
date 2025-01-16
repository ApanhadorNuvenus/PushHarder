package com.example.apptest.feature_train.presentation.trainingExercises.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.apptest.feature_train.domain.model.ExerciseSet
import com.example.apptest.feature_train.domain.model.TrainingExercise
import com.example.apptest.feature_train.domain.use_case.exercise_use_case.ExerciseUseCases
import com.example.apptest.feature_train.presentation.trainingExercises.TrainingExerciseWithSets

@Composable
fun ExtendedTrainingExerciseList(
    trainingExercisesWithSets: List<TrainingExerciseWithSets>,
    onAddSet: (TrainingExercise) -> Unit,
    onDeleteSet: (ExerciseSet) -> Unit,
    onDeleteTrainingExercise: (TrainingExercise) -> Unit,
    exerciseUseCases: ExerciseUseCases = hiltViewModel(),
) {

    LazyColumn {
        items(trainingExercisesWithSets) { item ->
            ExtendedTrainingExerciseItem(
                trainingExercise = item.trainingExercise,
                sets = item.sets,
                exerciseUseCases = exerciseUseCases,
                onAddSet = { onAddSet(item.trainingExercise) },
                onDeleteSet = { set -> onDeleteSet(set) },
                onDeleteTrainingExercise = {  onDeleteTrainingExercise(item.trainingExercise)  },
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}