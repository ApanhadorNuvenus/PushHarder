package com.example.apptest.feature_train.presentation.trainings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.apptest.feature_train.domain.model.Training
import com.example.apptest.feature_train.domain.use_case.exercise_use_case.ExerciseUseCases
import com.example.apptest.feature_train.presentation.trainings.TrainingsViewModel

@Composable
fun CoolTrainingsList(
    trainings: List<Training>,
    navController: NavController,
    onDeleteTraining: (Training) -> Unit,
    exerciseUseCases: ExerciseUseCases,
    modifier: Modifier = Modifier,
    viewModel: TrainingsViewModel = hiltViewModel()
) {
    Text("COOL TRAININ GS LIST")
    LazyColumn(
        modifier = modifier
    ) {
        items(trainings) { training ->
            CoolTrainingItem(
                training = training,
                navController = navController,
                onDeleteTraining = { onDeleteTraining(training) },
                exerciseUseCases = exerciseUseCases,
                viewModel = viewModel
            )
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}