package com.example.apptest.feature_train.presentation.util

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ScatterPlot
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.apptest.BuildConfig
import com.example.apptest.feature_train.presentation.add_edit_exercises.AddEditExercisesScreen
import com.example.apptest.feature_train.presentation.add_edit_exercises.AddEditExercisesScreen
import com.example.apptest.feature_train.presentation.add_edit_training.AddEditTrainingsScreen
import com.example.apptest.feature_train.presentation.exercises.ExercisesScreen
import com.example.apptest.feature_train.presentation.exercises.ExercisesViewModel
import com.example.apptest.feature_train.presentation.stats.StatsScreen
import com.example.apptest.feature_train.presentation.trainings.TrainingsScreen
import com.example.apptest.feature_train.presentation.trainings.TrainingsViewModel
import com.example.apptest.feature_train.presentation.util.Screen
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController,
    initialThemeMode: Int,
    onThemeModeChange: (Int) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = remember { mutableStateOf(Screen.TrainingsScreen.route) }
    var themeMode by remember { mutableStateOf(initialThemeMode) }

    // Update currentRoute whenever the back stack changes
    currentBackStackEntry.value?.let { entry ->
        currentRoute.value = entry.destination.route ?: Screen.TrainingsScreen.route
    }

    // Obtain TrainingsViewModel here
    val trainingsViewModel: TrainingsViewModel = hiltViewModel()

    // Collect events from EventBus and refresh trainings
    LaunchedEffect(key1 = EventBus.events) {
        EventBus.events.collect { event ->
            if (event is Event.ExerciseUpdated) {
                trainingsViewModel.refreshTrainings()
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Workout Tracker", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    NavigationDrawerItem(
                        label = { Text("Trainings") },
                        selected = currentRoute.value == Screen.TrainingsScreen.route,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(Screen.TrainingsScreen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
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
                        selected = currentRoute.value == Screen.ExercisesScreen.route,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(Screen.ExercisesScreen.route){
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
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
                        selected = currentRoute.value == Screen.StatsScreen.route,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(Screen.StatsScreen.route){
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.ScatterPlot,
                                contentDescription = "Statistics"
                            )
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Theme Settings", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    NavigationDrawerItem(
                        label = { Text("System Default") },
                        selected = themeMode == 0,
                        onClick = {
                            themeMode = 0
                            onThemeModeChange(0)
                            scope.launch { drawerState.close() }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Settings,
                                contentDescription = "System Default Theme"
                            )
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Light") },
                        selected = themeMode == 1,
                        onClick = {
                            themeMode = 1
                            onThemeModeChange(1)
                            scope.launch { drawerState.close() }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.LightMode,
                                contentDescription = "Light Theme"
                            )
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Dark") },
                        selected = themeMode == 2,
                        onClick = {
                            themeMode = 2
                            onThemeModeChange(2)
                            scope.launch { drawerState.close() }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.DarkMode,
                                contentDescription = "Dark Theme"
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
                        val title = when (currentRoute.value) {
                            Screen.TrainingsScreen.route -> "Trainings"
                            Screen.ExercisesScreen.route -> "Exercises"
                            Screen.StatsScreen.route -> "Statistics"
                            else -> "Trainings"
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