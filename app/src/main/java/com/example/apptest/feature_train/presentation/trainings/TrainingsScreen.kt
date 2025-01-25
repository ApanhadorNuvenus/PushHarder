package com.example.apptest.feature_train.presentation.trainings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.apptest.feature_train.domain.model.Training
import com.example.apptest.feature_train.presentation.trainings.components.CoolTrainingsList
import com.example.apptest.feature_train.presentation.trainings.components.LoadingOverlay
import com.example.apptest.feature_train.presentation.trainings.components.OrderSection
import com.example.apptest.feature_train.presentation.util.Screen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingsScreen(
    navController: NavController,
    viewModel: TrainingsViewModel = hiltViewModel()
) {
    val state by viewModel.state
    val trainings by viewModel.trainingsState.collectAsState()
    val pendingDeletionTrainings by viewModel.pendingDeletionTrainings.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDialog by remember { mutableStateOf(false) }
    var trainingToDelete by remember { mutableStateOf<Training?>(null) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Handle pending deletions when navigating away
    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect {
            viewModel.handlePendingDeletions()
        }
    }

    LaunchedEffect(pendingDeletionTrainings) {
        if (pendingDeletionTrainings.isNotEmpty()) {
            val training = pendingDeletionTrainings.last()
            val result = snackbarHostState.showSnackbar(
                message = "Training ${training.title} deleted",
                actionLabel = "Undo",
                duration = SnackbarDuration.Long
            )
            when (result) {
                SnackbarResult.ActionPerformed -> {
                    viewModel.onEvent(TrainingsEvent.RestoreTraining)
                }
                SnackbarResult.Dismissed -> {
                    trainingToDelete?.let { viewModel.confirmDeletion(it) }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddEditTrainingScreen.route)
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add training")
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text = "Trainings",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Button(onClick = { viewModel.onEvent(TrainingsEvent.ToggleOrderSection) }) {
                        Text("Order")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (state.isOrderSectionVisible) {
                    OrderSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        trainingOrder = state.trainingOrder,
                        onOrderChange = {
                            viewModel.onEvent(TrainingsEvent.Order(it))
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                CoolTrainingsList(
                    trainings = trainings,
                    onDeleteTraining = { training ->
                        viewModel.onEvent(TrainingsEvent.DeleteTraining(training))
                    },
                    navController = navController,
                    exerciseUseCases = viewModel.exerciseUseCases,
                    modifier = Modifier.weight(1f)
                )
            }

            LoadingOverlay(isVisible = isLoading)
        }
    }
}