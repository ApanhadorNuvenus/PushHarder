package com.croshapss.pushharder.feature_train.presentation.add_edit_training

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.croshapss.pushharder.feature_train.domain.model.Exercise
import com.croshapss.pushharder.feature_train.domain.model.TrainingExercise
import com.croshapss.pushharder.feature_train.presentation.add_edit_training.components.AddSetDialog
import com.croshapss.pushharder.feature_train.presentation.add_edit_training.components.TransparentHintTextField
import com.croshapss.pushharder.feature_train.presentation.trainingExercises.components.ExtendedTrainingExerciseList
import com.croshapss.pushharder.feature_train.presentation.util.Event
import com.croshapss.pushharder.feature_train.presentation.util.EventBus
import com.croshapss.pushharder.feature_train.presentation.util.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTrainingsScreen(
    navController: NavController,
    viewModel: AddEditTrainingsViewModel = hiltViewModel()
) {
    val trainingState = viewModel.trainingState.value
    val trainingExercisesWithSets by viewModel.trainingExercisesWithSets.collectAsState()
    val scaffoldState = remember { SnackbarHostState() }
    var showAddExerciseDialog by remember { mutableStateOf(false) }
    var showAddSetDialog by remember { mutableStateOf(false) }
    var selectedExerciseForSet by remember { mutableStateOf<TrainingExercise?>(null) }
    var exercises by remember { mutableStateOf(emptyList<Exercise>()) }

    // Collect exercises for the dropdown
    LaunchedEffect(key1 = viewModel) {
        viewModel.exerciseUseCases.getAllExercises().collect { exercises = it }
    }

    val exerciseCreated by remember(navController) {
        // Get StateFlow from savedStateHandle or use a default MutableStateFlow
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow<Boolean>("exerciseCreated", false)
            ?: MutableStateFlow(false)
    }.collectAsState()

    LaunchedEffect(exerciseCreated) {
        if (exerciseCreated) {
            showAddExerciseDialog = true
            // Reset the flag after showing the dialog
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.set("exerciseCreated", false)
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditTrainingsViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.showSnackbar(
                        message = event.message
                    )
                }

                is AddEditTrainingsViewModel.UiEvent.SaveTraining -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(scaffoldState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(AddEditTrainingsEvent.SaveTraining) },
                containerColor = MaterialTheme.colorScheme.primary,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp)
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save training")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Input Fields Container
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    TransparentHintTextField(
                        text = trainingState.title,
                        hint = "Enter title...",
                        onValueChange = { viewModel.onEvent(AddEditTrainingsEvent.EnteredTitle(it)) },
                        onFocusChange = { viewModel.onEvent(AddEditTrainingsEvent.ChangeTitleFocus(it)) },
                        isHintVisible = trainingState.isTitleHintVisible,
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

            }

            // Add Exercise Button
            Button(
                onClick = { showAddExerciseDialog = true },
                modifier = Modifier.fillMaxWidth(),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text("Add Exercise")
            }

            // AnimatedVisibility for Exercise List
            AnimatedVisibility(
                visible = trainingExercisesWithSets.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                ExtendedTrainingExerciseList(
                    trainingExercisesWithSets = trainingExercisesWithSets,
                    onAddSet = { trainingExercise ->
                        selectedExerciseForSet = trainingExercise
                        showAddSetDialog = true
                    },
                    onDeleteSet = { set ->
                        viewModel.onEvent(
                            AddEditTrainingsEvent.DeleteSetFromExercise(
                                set.trainingExerciseId,
                                set.id
                            )
                        )
                    },
                    onDeleteTrainingExercise = { trainingExercise ->
                        viewModel.onEvent(
                            AddEditTrainingsEvent.DeleteTrainingExercise(
                                trainingExercise.id
                            )
                        )
                    },
                    exerciseUseCases = viewModel.exerciseUseCases,
                    onWeightChanged = { trainingExerciseId, weight ->
                        viewModel.onEvent(AddEditTrainingsEvent.EnteredExerciseWeight(trainingExerciseId, weight))
                    },
                    onFailureChanged = { trainingExerciseId, failure ->
                        viewModel.onEvent(AddEditTrainingsEvent.ChangeExerciseFailureState(trainingExerciseId, failure))
                    },
                )
            }

            // Dialog for adding a new exercise
            if (showAddExerciseDialog) {
                AlertDialog(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    onDismissRequest = { showAddExerciseDialog = false },
                    title = { Text("Select Exercise to Add") },
                    text = {
                        if (exercises.isEmpty()) {
                            // If no exercises, show "Add Exercise" button
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "No exercises available.",
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                Button(
                                    onClick = {
                                        showAddExerciseDialog = false
                                        navController.navigate(Screen.AddEditExerciseScreen.route)
                                    },
                                    elevation = ButtonDefaults.buttonElevation(
                                        defaultElevation = 4.dp
                                    )
                                ) {
                                    Text("Add New Exercise")
                                }
                            }
                        } else {
                            LazyColumn {
                                items(exercises) { exercise ->
                                    Text(
                                        text = exercise.name,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                viewModel.onEvent(
                                                    AddEditTrainingsEvent.AddExercise(
                                                        exercise
                                                    )
                                                )
                                                showAddExerciseDialog = false
                                            }
                                            .padding(8.dp)
                                    )
                                }
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = { showAddExerciseDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }

            if (showAddSetDialog && selectedExerciseForSet != null) {
                AddSetDialog(
                    onDismiss = {
                        showAddSetDialog = false
                        selectedExerciseForSet = null
                    },
                    onConfirm = { newSet ->
                        selectedExerciseForSet?.let {
                            viewModel.onEvent(
                                AddEditTrainingsEvent.AddSetToExercise(
                                    trainingExerciseId = it.id,
                                    setNumber = newSet.setNumber,
                                    reps = newSet.reps ?: 0
                                )
                            )
                        }
                    }
                )
            }
        }
    }
}