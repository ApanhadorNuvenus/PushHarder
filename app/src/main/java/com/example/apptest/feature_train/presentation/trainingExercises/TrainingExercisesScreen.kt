//package com.example.apptest.feature_train.presentation.trainingExercises
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.Button
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.NavController
//import com.example.apptest.feature_train.domain.model.Exercise
//import com.example.apptest.feature_train.domain.model.ExerciseType
//import com.example.apptest.feature_train.domain.model.TrainingExercise
//import com.example.apptest.feature_train.presentation.add_edit_training.ExerciseDetailsDialog
//import com.example.apptest.feature_train.presentation.add_edit_training.AddEditTrainingsEvent
//import androidx.compose.foundation.layout.fillMaxWidth
//import com.example.apptest.feature_train.domain.util.ExerciseOrder
//import com.example.apptest.feature_train.domain.util.OrderType
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TrainingExercisesScreen(
//    navController: NavController,
//    trainingId: String,
//    viewModel: TrainingExercisesViewModel = hiltViewModel()
//) {
//    val state = viewModel.state.value
//    var showDialog by remember { mutableStateOf(false) }
//    var selectedExercise by remember { mutableStateOf<Exercise?>(null) }
//    var exercises by remember { mutableStateOf(emptyList<Exercise>()) }
//    var expanded by remember { mutableStateOf(false) }
//
//    // Collect exercises for the dropdown
//    LaunchedEffect(key1 = viewModel) {
//        viewModel.exerciseUseCases.getAllExercises(ExerciseOrder.Type(OrderType.Descending))
//            .collect { exercises = it }
//    }
//
//    // Function to show the dialog
//    fun onExerciseSelect(exercise: Exercise) {
//        selectedExercise = exercise
//        showDialog = true
//    }
//
//    // Dialog composable
//    if (showDialog) {
//        selectedExercise?.let { exercise ->
//            ExerciseDetailsDialog(
//                exercise = exercise,
//                onDismiss = { showDialog = false },
//                onConfirm = { reps, duration ->
//                    showDialog = false
//                    viewModel.onEvent(TrainingExercisesEvent.AddExercise(trainingId, exercise, reps, duration))
//                }
//            )
//        }
//    }
//
//    Column(modifier = Modifier.padding(16.dp)) {
//        // Button to add exercises
//        Button(
//            onClick = { expanded = !expanded },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Add Exercise")
//        }
//
//        // Dropdown menu for exercise selection
//        if (expanded) {
//            AlertDialog(
//                onDismissRequest = { expanded = false },
//                title = { Text("Select an Exercise") },
//                text = {
//                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
//                        items(exercises) { exercise ->
//                            DropdownMenuItem(
//                                text = {
//                                    Column {
//                                        Text(exercise.name)
//                                        when (exercise.exerciseType) {
//                                            is ExerciseType.Reps -> Text("Reps")
//                                            is ExerciseType.Duration -> Text("Duration")
//                                        }
//                                    }
//                                },
//                                onClick = {
//                                    onExerciseSelect(exercise)
//                                    expanded = false
//                                }
//                            )
//                        }
//                    }
//                },
//                confirmButton = {
//                    Button(onClick = { expanded = false }) {
//                        Text("Cancel")
//                    }
//                }
//            )
//        }
//        LazyColumn(modifier = Modifier.weight(1f)) {
//            items(state.trainingExercises) { trainingExercise ->
//                TrainingExerciseItem(
//                    trainingExercise = trainingExercise,
//                    viewModel = viewModel
//                )
//            }
//        }
//        Button(
//            onClick = {
//                navController.navigateUp() // Go back to previous screen
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Done")
//        }
//    }
//}
//
//@Composable
//fun TrainingExerciseItem(
//    trainingExercise: TrainingExercise,
//    viewModel: TrainingExercisesViewModel
//) {
//    val exercise by viewModel.getExercise(trainingExercise.exerciseId).collectAsState(initial = null)
//
//    Column(modifier = Modifier.padding(8.dp)) {
//        exercise?.let {
//            Text(it.name, style = MaterialTheme.typography.bodyLarge)
//            when (it.exerciseType) {
//                is ExerciseType.Reps -> {
//                    trainingExercise.reps?.let { reps ->
//                        Text("Reps: $reps", style = MaterialTheme.typography.bodyMedium)
//                    }
//                }
//                is ExerciseType.Duration -> {
//                    trainingExercise.duration?.let { duration ->
//                        Text("Duration: $duration seconds", style = MaterialTheme.typography.bodyMedium)
//                    }
//                }
//            }
//        }
//        HorizontalDivider()
//    }
//}