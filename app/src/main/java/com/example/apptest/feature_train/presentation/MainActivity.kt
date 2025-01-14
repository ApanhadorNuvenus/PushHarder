package com.example.apptest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.apptest.feature_train.presentation.add_edit_exercise.AddEditExercisesScreen
import com.example.apptest.feature_train.presentation.add_edit_training.AddEditTrainingsScreen
import com.example.apptest.feature_train.presentation.exercises.ExercisesScreen
import com.example.apptest.feature_train.presentation.trainings.TrainingsScreen
import com.example.apptest.feature_train.presentation.util.Screen
import com.example.apptest.ui.theme.AppTestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTestTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.TrainingsScreen.route
//                          startDestination = Screen.ExercisesScreen.route
                    ) {
                        composable(route = Screen.TrainingsScreen.route) {
                            TrainingsScreen(navController = navController)
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
                        // EXERCISES
                        composable(route = Screen.ExercisesScreen.route) {
                            ExercisesScreen(navController = navController)
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
    }
}