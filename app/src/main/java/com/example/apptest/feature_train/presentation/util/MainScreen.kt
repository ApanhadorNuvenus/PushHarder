package com.example.apptest.feature_train.presentation.util

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ScatterPlot
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.apptest.BuildConfig
import com.example.apptest.feature_train.presentation.add_edit_exercise.AddEditExercisesScreen
import com.example.apptest.feature_train.presentation.add_edit_training.AddEditTrainingsScreen
import com.example.apptest.feature_train.presentation.exercises.ExercisesScreen
import com.example.apptest.feature_train.presentation.stats.StatsScreen
import com.example.apptest.feature_train.presentation.trainings.TrainingsScreen
import com.example.apptest.feature_train.presentation.util.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Workout Tracker", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    NavigationDrawerItem(
                        label = { Text("Trainings") },
                        selected = navController.currentDestination?.route == Screen.TrainingsScreen.route,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(Screen.TrainingsScreen.route) {
                                popUpTo(0)
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Trainings"
                            )
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Exercises") },
                        selected = navController.currentDestination?.route == Screen.ExercisesScreen.route,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(Screen.ExercisesScreen.route){
                                popUpTo(0)
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.FitnessCenter,
                                contentDescription = "Exercises"
                            )
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Statistics") },
                        selected = navController.currentDestination?.route == Screen.StatsScreen.route,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(Screen.StatsScreen.route){
                                popUpTo(0)
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.ScatterPlot,
                                contentDescription = "Statistics"
                            )
                        }
                    )
                    Spacer(modifier = Modifier.weight(1f)) // Push the version info to the bottom
                    Text(
                        text = "Version: ${BuildConfig.VERSION_NAME}", // Display app version
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        val currentRoute = navController.currentDestination?.route
                        val title = when (currentRoute) {
                            Screen.TrainingsScreen.route -> "Trainings"
                            Screen.ExercisesScreen.route -> "Exercises"
                            Screen.StatsScreen.route -> "Statistics"
                            else -> "<is that even a screen>?"
                        }
                        Text(title)
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Open drawer")
                        }
                    }
                )
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = Screen.TrainingsScreen.route,
                modifier = Modifier.padding(padding)
            ) {
                composable(route = Screen.TrainingsScreen.route) {
                    TrainingsScreen(navController = navController)
                }
                composable(route = Screen.ExercisesScreen.route) {
                    ExercisesScreen(navController = navController)
                }
                composable(route = Screen.StatsScreen.route) {
                    StatsScreen(navController = navController)
                }
                composable(
                    route = Screen.AddEditTrainingScreen.route +
                            "?trainingId={trainingId}",
                    arguments = listOf(
                        navArgument(
                            name = "trainingId"
                        ) {
                            type = NavType.StringType
                            defaultValue = ""
                        }
                    )
                ) {
                    AddEditTrainingsScreen(
                        navController = navController
                    )
                }
                composable(
                    route = Screen.AddEditExerciseScreen.route +
                            "?exerciseId={exerciseId}",
                    arguments = listOf(
                        navArgument(
                            name = "exerciseId"
                        ) {
                            type = NavType.IntType
                            defaultValue = -1
                        }
                    )
                ) {
                    AddEditExercisesScreen(
                        navController = navController
                    )
                }
            }
        }
    }
}