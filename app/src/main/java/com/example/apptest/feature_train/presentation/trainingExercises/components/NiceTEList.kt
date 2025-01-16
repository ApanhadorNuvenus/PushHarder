package com.example.apptest.feature_train.presentation.trainingExercises.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.apptest.feature_train.presentation.trainingExercises.TrainingExerciseWithSets
import com.example.apptest.feature_train.presentation.trainings.components.NiceTEItem

@Composable
fun NiceTEList(
    trainingExercisesWithSets: List<TrainingExerciseWithSets>,
    modifier: Modifier = Modifier,
    //    exerciseUseCases: ExerciseUseCases = hiltViewModel() // not needed
) {
    Column(
        modifier = modifier
    ) {
        trainingExercisesWithSets.forEach { trainingExerciseWithSets ->
            NiceTEItem(
                exercise = trainingExerciseWithSets.exercise,
                sets = trainingExerciseWithSets.sets
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}