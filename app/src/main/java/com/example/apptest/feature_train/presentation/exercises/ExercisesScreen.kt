package com.example.apptest.feature_train.presentation.exercises

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.apptest.feature_train.domain.util.ExerciseOrder
import com.example.apptest.feature_train.domain.util.OrderType
import com.example.apptest.feature_train.presentation.exercises.components.ExerciseItem
//import com.example.apptest.feature_train.presentation.exercises.components.ExerciseOrderSection
//import com.example.apptest.feature_train.presentation.exercises.components.ExerciseOrderSection
import com.example.apptest.feature_train.presentation.util.Screen
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.apptest.feature_train.domain.model.Exercise

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExercisesScreen(
    navController: NavController,
    viewModel: ExercisesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val pendingDeletionExercises by viewModel.pendingDeletionExercises.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var exerciseToDelete by remember { mutableStateOf<Exercise?>(null) }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val isExercisesScreen = currentBackStackEntry?.destination?.route == Screen.ExercisesScreen.route

    LaunchedEffect(key1 = pendingDeletionExercises, key2 = isExercisesScreen) {
        if (pendingDeletionExercises.isNotEmpty() && isExercisesScreen) {
            val exercise = pendingDeletionExercises.last()
            val result = snackbarHostState.showSnackbar(
                message = "Exercise ${exercise.name} deleted",
                actionLabel = "Undo",
                duration = SnackbarDuration.Short
            )
            when (result) {
                SnackbarResult.ActionPerformed -> {
                    viewModel.onEvent(ExercisesEvent.RestoreExercise)
                }
                SnackbarResult.Dismissed -> {
                    viewModel.confirmDeletion()
                }
            }
        }
    }

    // Handle pending deletions when navigating away
    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect {
            if(it.destination.route != Screen.ExercisesScreen.route) {
                viewModel.handlePendingDeletions()
                Log.d("ExerciseDelete", "navigated from exercise screen")
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddEditExerciseScreen.route)
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add exercise"
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            AnimatedVisibility(
                visible = state.isOrderSectionVisible,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.exercises.filter { ex -> !pendingDeletionExercises.map { it.id }.contains(ex.id) }) { exercise ->
                    ExerciseItem(
                        exercise = exercise,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    Screen.AddEditExerciseScreen.route +
                                            "?exerciseId=${exercise.id}"
                                )
                            },
                        onDeleteClick = {
                            exerciseToDelete = exercise
                            showDeleteConfirmation = true
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            if (showDeleteConfirmation) {
                AlertDialog(
                    onDismissRequest = { showDeleteConfirmation = false },
                    title = { Text("Confirm Delete") },
                    text = { Text("This exercise will be removed from all trainings. Are you sure?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                exerciseToDelete?.let {
                                    viewModel.onEvent(ExercisesEvent.DeleteExercise(it))
                                }
                                showDeleteConfirmation = false
                            }
                        ) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showDeleteConfirmation = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}