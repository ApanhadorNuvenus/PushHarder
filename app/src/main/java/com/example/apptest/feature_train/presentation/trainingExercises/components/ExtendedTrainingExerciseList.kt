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

@Composable
fun ExtendedTrainingExerciseList(
    trainingExercises: List<TrainingExercise>,
    trainingExerciseSets: Map<String, List<ExerciseSet>>,
    onAddSet: (TrainingExercise) -> Unit,
    onDeleteSet: (ExerciseSet) -> Unit,
    onDeleteTrainingExercise: (TrainingExercise) -> Unit,
    exerciseUseCases: ExerciseUseCases = hiltViewModel(),
//    onDeleteAllSets: (TrainingExercise) -> Unit
) {

    LazyColumn {
        items(trainingExercises) { trainingExercise ->
            val sets = trainingExerciseSets[trainingExercise.id].orEmpty()
            ExtendedTrainingExerciseItem(
                trainingExercise = trainingExercise,
                sets = sets,
                exerciseUseCases = exerciseUseCases,
                onAddSet = { onAddSet(trainingExercise) },
                onDeleteSet = { set -> onDeleteSet(set) },
                onDeleteTrainingExercise = { onDeleteTrainingExercise(trainingExercise) },
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}