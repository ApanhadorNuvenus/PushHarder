package com.example.apptest.feature_train.presentation.trainings.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.apptest.feature_train.domain.model.Training
import com.example.apptest.feature_train.presentation.trainingExercises.TrainingExerciseWithSets

@Composable
fun NiceTrainingsList(
    trainings: List<Training>,
    trainingExercisesWithSets: Map<String, List<TrainingExerciseWithSets>>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(trainings) { training ->
            val exercisesForTraining = trainingExercisesWithSets[training.id].orEmpty()
            NiceTrainingItem(
                training = training,
                trainingExercisesWithSets = exercisesForTraining,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}