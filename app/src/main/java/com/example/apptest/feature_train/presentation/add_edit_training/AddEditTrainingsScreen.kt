package com.example.apptest.feature_train.presentation.add_edit_training

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.apptest.feature_train.domain.model.Exercise
import com.example.apptest.feature_train.domain.model.TrainingExercise
import com.example.apptest.feature_train.presentation.add_edit_training.components.TransparentHintTextField
import com.example.apptest.feature_train.presentation.trainingExercises.components.ExtendedTrainingExerciseList
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTrainingsScreen(
    navController: NavController,
    viewModel: AddEditTrainingsViewModel = hiltViewModel()
) {
    val trainingState = viewModel.trainingState.value
    val scaffoldState = remember { SnackbarHostState() }
    var showAddExerciseDialog by remember { mutableStateOf(false) }
    var showAddSetDialog by remember { mutableStateOf(false) }
    var selectedExercise by remember { mutableStateOf<Exercise?>(null) }
    var selectedTrainingExercise by remember { mutableStateOf<TrainingExercise?>(null) }
    var exercises by remember { mutableStateOf(emptyList<Exercise>()) }
    val trainingExerciseSets by viewModel.trainingExerciseSets.collectAsState()

    val scope = rememberCoroutineScope()

    // Collect exercises for the dropdown
    LaunchedEffect(key1 = viewModel) {
        viewModel.exerciseUseCases.getAllExercises().collect { exercises = it }
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditTrainingsViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.showSnackbar(
                        message = event.message
                    )
                }
                is AddEditTrainingsViewModel.UiEvent.PromptForSet -> {
                    selectedTrainingExercise = event.trainingExercise
                    showAddSetDialog = true
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(scaffoldState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddExerciseDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add exercise")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
                .padding(16.dp)
        ) {
            TransparentHintTextField(
                text = trainingState.title,
                hint = "Enter title...",
                onValueChange = { viewModel.onEvent(AddEditTrainingsEvent.EnteredTitle(it)) },
                onFocusChange = { viewModel.onEvent(AddEditTrainingsEvent.ChangeTitleFocus(it)) },
                isHintVisible = trainingState.isTitleHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            TransparentHintTextField(
                text = trainingState.weights ?: "",
                hint = "Enter weights...",
                onValueChange = { viewModel.onEvent(AddEditTrainingsEvent.EnteredWeight(it)) },
                onFocusChange = { viewModel.onEvent(AddEditTrainingsEvent.ChangeWeightFocus(it)) },
                isHintVisible = trainingState.isWeightHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = trainingState.failure,
                    onCheckedChange = { viewModel.onEvent(AddEditTrainingsEvent.ChangeFailureState(it)) }
                )
                Text(text = "Failure")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Using ExtendedTrainingExerciseList
            ExtendedTrainingExerciseList(
                trainingExercises = trainingState.trainingExercises,
                trainingExerciseSets = trainingExerciseSets,
                onAddSet = { trainingExercise ->
                    selectedTrainingExercise = trainingExercise
                    showAddSetDialog = true
                },
                onDeleteSet = {  set ->
                    viewModel.onEvent(AddEditTrainingsEvent.DeleteSet(set))
                },
                onDeleteTrainingExercise = { trainingExercise ->
                    viewModel.onEvent(AddEditTrainingsEvent.DeleteTrainingExercise(trainingExercise))
                },
                exerciseUseCases = viewModel.exerciseUseCases
            )

            // Dialog for adding a new exercise
            if (showAddExerciseDialog) {
                AlertDialog(
                    onDismissRequest = { showAddExerciseDialog = false },
                    title = { Text("Select Exercise to Add") },
                    text = {
                        LazyColumn {
                            items(exercises) { exercise ->
                                Text(
                                    text = exercise.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedExercise = exercise
                                            showAddExerciseDialog = false
                                            viewModel.onEvent(AddEditTrainingsEvent.AddExercise(exercise))
                                        }
                                        .padding(8.dp)
                                )
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            selectedExercise?.let {
                                viewModel.onEvent(AddEditTrainingsEvent.AddExercise(it))
                            }
                            showAddExerciseDialog = false
                        }) {
                            Text("Add")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showAddExerciseDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }

            if (showAddSetDialog) {
                var newSetNumber by remember { mutableStateOf(1) }
                var newSetReps by remember { mutableStateOf(0) }

                AlertDialog(
                    onDismissRequest = { showAddSetDialog = false },
                    title = { Text("Add New Set") },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = newSetNumber.toString(),
                                onValueChange = { newValue ->
                                    newSetNumber = newValue.toIntOrNull() ?: 1
                                },
                                label = { Text("Set Number") }
                            )
                            OutlinedTextField(
                                value = newSetReps.toString(),
                                onValueChange = { newValue ->
                                    newSetReps = newValue.toIntOrNull() ?: 0
                                },
                                label = { Text("Reps") }
                            )
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            selectedTrainingExercise?.let { trainingExercise ->
                                viewModel.onEvent(AddEditTrainingsEvent.AddSet(trainingExercise, newSetNumber, newSetReps))
                                showAddSetDialog = false
                            }
                        }) {
                            Text("Add")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showAddSetDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}