package com.example.apptest.feature_train.presentation.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.apptest.feature_train.domain.model.Training
import com.example.apptest.feature_train.presentation.stats.components.CoolStatsTrainingItem
import com.example.apptest.feature_train.presentation.stats.components.ExerciseDropdown
import com.example.apptest.feature_train.presentation.stats.components.StatsPlot
import com.example.apptest.feature_train.presentation.trainings.components.CoolTrainingItem

@Composable
fun StatsScreen(
    viewModel: StatsViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()
    var selectedTraining by remember { mutableStateOf<Training?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Statistics", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        ExerciseDropdown(
            exercises = state.allExercises,
            selectedExerciseName = state.selectedExerciseName,
            onExerciseSelected = { exerciseName ->
                viewModel.onEvent(StatsEvent.SelectExercise(exerciseName))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        StatsPlot(
            trainings = state.filteredTrainings,
            selectedExerciseName = state.selectedExerciseName,
            trainingExercisesWithSets = state.trainingExercisesWithSets,
            onTrainingSelected = { training -> selectedTraining = training }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display CoolTrainingItem only if a training is selected
        selectedTraining?.let { training ->
            CoolStatsTrainingItem(
                training = training,
                exerciseUseCases = viewModel.exerciseUseCases,
                viewModel = hiltViewModel(), // Pass the StatsViewModel or another relevant VM if needed
                modifier = Modifier.fillMaxWidth(),
                selectedExerciseName = state.selectedExerciseName
            )
        }
    }
}