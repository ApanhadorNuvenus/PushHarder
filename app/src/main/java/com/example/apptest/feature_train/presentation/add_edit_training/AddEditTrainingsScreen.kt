package com.example.apptest.feature_train.presentation.add_edit_training

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.apptest.feature_train.presentation.add_edit_training.components.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.apptest.feature_train.domain.model.Exercise
import com.example.apptest.feature_train.domain.model.ExerciseType
import com.example.apptest.feature_train.domain.util.ExerciseOrder
import com.example.apptest.feature_train.domain.util.OrderType
import com.example.apptest.feature_train.presentation.add_edit_training.components.ExerciseDetailsDialog
import com.example.apptest.feature_train.presentation.util.Screen
import com.example.apptest.feature_train.presentation.trainingExercises.TrainingExercisesEvent
import com.example.apptest.feature_train.presentation.trainingExercises.TrainingExercisesViewModel
import com.example.apptest.feature_train.presentation.trainingExercises.components.TrainingExerciseItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTrainingsScreen(
    navController: NavController,
    viewModel: AddEditTrainingsViewModel = hiltViewModel(),
) {
    val titleState = viewModel.trainingState.value
    val weightsState = viewModel.trainingState.value
    val failureState = viewModel.trainingState.value
    val scaffoldState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var selectedExercise by remember { mutableStateOf<Exercise?>(null) }
    var exercises by remember { mutableStateOf(emptyList<Exercise>()) }
    var expanded by remember { mutableStateOf(false) }
    var isTitleEditMode by remember { mutableStateOf(false) }

    // Collect the currentTrainingId as state
    val currentTrainingId by viewModel.currentTrainingId.collectAsState()

    // Collect exercises for the dropdown
    LaunchedEffect(key1 = viewModel) {
        viewModel.exerciseUseCases.getAllExercises(ExerciseOrder.Type(OrderType.Descending))
            .collect { exercises = it }
    }

    // Function to show the dialog
    fun onExerciseSelect(exercise: Exercise) {
        selectedExercise = exercise
        showDialog = true
    }

    // Dialog composable
    if (showDialog) {
        selectedExercise?.let { exercise ->
            ExerciseDetailsDialog(
                exercise = exercise,
                onDismiss = { showDialog = false },
                onConfirm = { reps, duration ->
                    showDialog = false
                    viewModel.onEvent(
                        AddEditTrainingsEvent.AddExercise(
                            exercise,
                            reps,
                            duration
                        )
                    )
                }
            )
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(scaffoldState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(AddEditTrainingsEvent.SaveTraining) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save training")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Title input logic
            if (isTitleEditMode || titleState.title.isEmpty() || titleState.title.startsWith("Temp-")) {
                // Edit mode: Show TransparentHintTextField
                TransparentHintTextField(
                    text = if (titleState.title.startsWith("Temp-")) "" else titleState.title,
                    hint = "Enter title...",
                    isHintVisible = titleState.isTitleHintVisible,
                    onValueChange = {
                        viewModel.onEvent(AddEditTrainingsEvent.EnteredTitle(it))
                        isTitleEditMode = true
                    },
                    onFocusChange = {
                        viewModel.onEvent(AddEditTrainingsEvent.ChangeTitleFocus(it))
                        isTitleEditMode = it.isFocused
                    },
                    textStyle = MaterialTheme.typography.headlineSmall,
                    singleLine = true
                )
            } else {
                // Display mode: Show title as Text
                Text(
                    text = titleState.title,
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Weights input
            TransparentHintTextField(
                text = weightsState.weights ?: "",
                hint = "Enter weights...",
                isHintVisible = weightsState.isWeightHintVisible,
                onValueChange = { viewModel.onEvent(AddEditTrainingsEvent.EnteredWeight(it)) },
                onFocusChange = { viewModel.onEvent(AddEditTrainingsEvent.ChangeWeightFocus(it)) },
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = true
            )

            // Failure checkbox
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = failureState.failure,
                    onCheckedChange = { viewModel.onEvent(AddEditTrainingsEvent.ChangeFailureState(it)) }
                )
                Text("Failure")
            }
            // Button to add exercises
            Button(
                onClick = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Exercise")
            }

            // Dropdown menu for exercise selection
            if (expanded) {
                AlertDialog(
                    onDismissRequest = { expanded = false },
                    title = { Text("Select an Exercise") },
                    text = {
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(exercises) { exercise ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(exercise.name)
                                            when (exercise.exerciseType) {
                                                is ExerciseType.Reps -> Text("Reps")
                                                is ExerciseType.Duration -> Text("Duration")
                                            }
                                        }
                                    },
                                    onClick = {
                                        onExerciseSelect(exercise)
                                        expanded = false
                                    }
                                )
                            }
                        }
                    },
                    confirmButton = {
                        Button(onClick = { expanded = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
            // Collect and display training exercises for the given trainingId
            val trainingExercises = viewModel.trainingState.value.temporaryExercises
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(trainingExercises) { trainingExercise ->
                    val exercise by viewModel.getExercise(trainingExercise.exerciseId)
                        .collectAsState(initial = null)
                    TrainingExerciseItem(
                        exerciseName = exercise?.name ?: "Loading...",
                        exerciseType = exercise?.exerciseType ?: ExerciseType.Reps,
                        reps = trainingExercise.reps,
                        duration = trainingExercise.duration,
                        onDelete = {
                            viewModel.onEvent(
                                AddEditTrainingsEvent.DeleteTemporaryExercise(
                                    trainingExercise
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    // Listen for the save success event and navigate up
    LaunchedEffect(key1 = viewModel.eventFlow) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is AddEditTrainingsViewModel.UiEvent.SaveTraining -> {
                    navController.navigateUp()
                }
                is AddEditTrainingsViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.showSnackbar(
                        message = event.message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }
}