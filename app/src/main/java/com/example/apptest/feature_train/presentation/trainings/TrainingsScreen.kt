package com.example.apptest.feature_train.presentation.trainings

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
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
import com.example.apptest.BuildConfig
import com.example.apptest.feature_train.domain.model.Training
import com.example.apptest.feature_train.presentation.trainings.components.OrderSection
import com.example.apptest.feature_train.presentation.trainings.components.TrainingItem
import com.example.apptest.feature_train.presentation.trainingExercises.components.TrainingExerciseList
import com.example.apptest.feature_train.presentation.trainings.components.CoolTrainingsList
import com.example.apptest.feature_train.presentation.util.Screen
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.filled.ScatterPlot
import com.example.apptest.feature_train.presentation.trainings.components.LoadingOverlay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainingsScreen(
    navController: NavController,
    viewModel: TrainingsViewModel = hiltViewModel()
) {
    val state by viewModel.state
    val trainings by viewModel.trainingsState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState() // Collect the isLoading state
    var showDialog by remember { mutableStateOf(false) }
    var trainingToDelete by remember { mutableStateOf<Training?>(null) }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    Scaffold(
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
        Box(modifier = Modifier.fillMaxSize()) { // Add a Box to contain both content and overlay
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

                Spacer(modifier = Modifier.height(8.dp)) // Reduced spacing

                if (state.isOrderSectionVisible) {
                    OrderSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp), // Reduced padding
                        trainingOrder = state.trainingOrder,
                        onOrderChange = {
                            viewModel.onEvent(TrainingsEvent.Order(it))
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp)) // Reduced spacing

                CoolTrainingsList(
                    trainings = trainings,
                    onDeleteTraining = { training ->
                        trainingToDelete = training
                        showDialog = true
                    },
                    navController = navController,
                    exerciseUseCases = viewModel.exerciseUseCases,
                    modifier = Modifier.weight(1f)
                )

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Confirm Deletion") },
                        text = { Text("Are you sure you want to delete this training?") },
                        confirmButton = {
                            Button(
                                onClick = {
                                    trainingToDelete?.let {
                                        viewModel.onEvent(TrainingsEvent.DeleteTraining(it))
                                    }
                                    showDialog = false
                                    trainingToDelete = null
                                }
                            ) {
                                Text("Delete")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    showDialog = false
                                    trainingToDelete = null
                                }
                            ) {
                                Text("Cancel")
                            }
                        }
                    )
                }
            }

            // Conditionally display the LoadingOverlay
            LoadingOverlay(isVisible = isLoading)
        }
    }
}