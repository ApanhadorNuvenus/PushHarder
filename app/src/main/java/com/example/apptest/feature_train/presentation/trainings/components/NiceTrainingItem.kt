package com.example.apptest.feature_train.presentation.trainings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.apptest.feature_train.domain.model.Training
import com.example.apptest.feature_train.presentation.trainingExercises.TrainingExerciseWithSets
import com.example.apptest.feature_train.presentation.trainingExercises.components.NiceTEList

@Composable
fun NiceTrainingItem(
    training: Training,
    trainingExercisesWithSets: List<TrainingExerciseWithSets>,
    modifier: Modifier = Modifier
) {
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
            Text(
                text = training.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            NiceTEList(
                trainingExercisesWithSets = trainingExercisesWithSets,
            )
        }
    }
}