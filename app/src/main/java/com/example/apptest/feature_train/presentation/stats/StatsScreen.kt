package com.example.apptest.feature_train.presentation.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    val selectedTrainingId by viewModel.selectedTrainingId.collectAsState()

    // LaunchedEffect to trigger data reload when selectedExerciseName changes
    LaunchedEffect(state.selectedExerciseName) {
        state.selectedExerciseName?.let { viewModel.filterTrainingsByExercise(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
//        Text("Statistics", style = MaterialTheme.typography.headlineMedium)

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
            onTrainingSelected = { training ->
                training?.let { viewModel.updateSelectedTraining(it.id) }
            },
            allExercises = state.allExercises
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Improved "No Trainings" Message
        if (state.filteredTrainings.isEmpty() && !state.selectedExerciseName.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(
                        "No trainings use ${state.selectedExerciseName}",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        } else {
            // Display CoolTrainingItem if a training with selected exercise is found
            selectedTrainingId?.let { trainingId ->
                val selectedTraining = state.filteredTrainings.firstOrNull { it.id == trainingId }
                selectedTraining?.let { training ->
                    CoolStatsTrainingItem(
                        training = training,
                        exerciseUseCases = viewModel.exerciseUseCases,
                        viewModel = hiltViewModel(),
                        modifier = Modifier.fillMaxWidth(),
                        selectedExerciseName = state.selectedExerciseName
                    )
                }
            }
        }
    }
}