package com.croshapss.pushharder.feature_train.presentation.trainings

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.croshapss.pushharder.feature_train.domain.model.Training
import com.croshapss.pushharder.feature_train.presentation.trainings.components.CoolTrainingsList
import com.croshapss.pushharder.feature_train.presentation.trainings.components.LoadingOverlay
import com.croshapss.pushharder.feature_train.presentation.trainings.components.OrderSection
import com.croshapss.pushharder.feature_train.presentation.util.Screen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingsScreen(
    navController: NavController,
    viewModel: TrainingsViewModel
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
                // Row and Text title REMOVED from here

                AnimatedVisibility( // <---- AnimatedVisibility for OrderSection
                    visible = state.isOrderSectionVisible,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
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

                val spacerHeightDp: Dp by animateDpAsState( // <---- Animated Spacer Height
                    targetValue = if (state.isOrderSectionVisible) 8.dp else 0.dp,
                    animationSpec = tween(durationMillis = 300) // Match AnimatedVisibility duration for smoothness
                )

                Spacer(modifier = Modifier.height(spacerHeightDp)) // <---- Animated Spacer

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