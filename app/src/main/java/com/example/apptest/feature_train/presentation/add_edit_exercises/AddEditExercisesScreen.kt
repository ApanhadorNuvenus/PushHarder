package com.example.apptest.feature_train.presentation.add_edit_exercises

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
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
    val goalState = viewModel.exerciseState.value

    val scaffoldState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
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

            // Goal
            TransparentHintTextField(
                text = goalState.goal?.toString() ?: "",
                hint = "Enter goal...",
                isHintVisible = goalState.isGoalHintVisible,
                onValueChange = {
                    viewModel.onEvent(AddEditExercisesEvent.EnteredGoal(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditExercisesEvent.ChangeGoalFocus(it))
                },
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}