package com.example.apptest.feature_train.presentation.add_edit_exercise

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.apptest.feature_train.domain.model.ExerciseType
import com.example.apptest.feature_train.presentation.add_edit_training.components.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditExercisesScreen(
    navController: NavController,
    viewModel: AddEditExercisesViewModel = hiltViewModel()
) {
    val titleState = viewModel.exerciseState.value
    val descriptionState = viewModel.exerciseState.value
//    val exerciseTypeState = viewModel.exerciseState.value.exerciseType

    val scaffoldState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is AddEditExercisesViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.showSnackbar(
                        message = event.message
                    )
                }
                is AddEditExercisesViewModel.UiEvent.SaveExercise -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddEditExercisesEvent.SaveExercise)
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "Save exercise"
                )
            }
        },
        snackbarHost = { SnackbarHost(scaffoldState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Title
            TransparentHintTextField(
                text = titleState.title,
                hint = "Enter title...",
                isHintVisible = titleState.isTitleHintVisible,
                onValueChange = {
                    viewModel.onEvent(AddEditExercisesEvent.EnteredTitle(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditExercisesEvent.ChangeTitleFocus(it))
                },
                textStyle = MaterialTheme.typography.headlineSmall,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Description
            TransparentHintTextField(
                text = descriptionState.description,
                hint = "Enter description...",
                isHintVisible = descriptionState.isDescriptionHintVisible,
                onValueChange = {
                    viewModel.onEvent(AddEditExercisesEvent.EnteredDescription(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditExercisesEvent.ChangeDescriptionFocus(it))
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = false,
                modifier = Modifier.height(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Exercise Type Selection
//            ExerciseTypeSelector(
//                exerciseType = exerciseTypeState,
//                onTypeChange = {
//                    viewModel.onEvent(AddEditExercisesEvent.ChangeExerciseType(it))
//                }
//            )
        }
    }
}

//@Composable
//fun ExerciseTypeSelector(
//    exerciseType: ExerciseType,
//    onTypeChange: (ExerciseType) -> Unit
//) {
//    var selectedType by remember { mutableStateOf(exerciseType) }
//
//    Column {
//        Text("Exercise Type:", style = MaterialTheme.typography.bodyMedium)
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Row {
//            RadioButton(
//                selected = selectedType is ExerciseType.Reps,
//                onClick = {
//                    selectedType = ExerciseType.Reps
//                    onTypeChange(selectedType)
//                },
//                colors = RadioButtonDefaults.colors(
//                    selectedColor = MaterialTheme.colorScheme.primary,
//                    unselectedColor = MaterialTheme.colorScheme.onBackground
//                )
//            )
//            Text("Reps")
//
//            Spacer(modifier = Modifier.width(16.dp))
//
//            RadioButton(
//                selected = selectedType is ExerciseType.Duration,
//                onClick = {
//                    selectedType = ExerciseType.Duration
//                    onTypeChange(selectedType)
//                },
//                colors = RadioButtonDefaults.colors(
//                    selectedColor = MaterialTheme.colorScheme.primary,
//                    unselectedColor = MaterialTheme.colorScheme.onBackground
//                )
//            )
//            Text("Duration")
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))

//        when (selectedType) {
//            is ExerciseType.Reps -> {
//                var reps by remember { mutableStateOf(if (exerciseType is ExerciseType.Reps) exerciseType.reps else 0) }
//                TextField(
//                    value = reps.toString(),
//                    onValueChange = { newValue ->
//                        reps = newValue.toIntOrNull() ?: 0
//                        onTypeChange(ExerciseType.Reps(reps))
//                    },
//                    label = { Text("Reps") },
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//                )
//            }
//            is ExerciseType.Duration -> {
//                var duration by remember { mutableStateOf(if (exerciseType is ExerciseType.Duration) exerciseType.time else 0) }
//                TextField(
//                    value = duration.toString(),
//                    onValueChange = { newValue ->
//                        duration = newValue.toIntOrNull() ?: 0
//                        onTypeChange(ExerciseType.Duration(duration))
//                    },
//                    label = { Text("Duration (seconds)") },
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//                )
//            }
//        }
//    }
//}