package com.example.apptest.feature_train.presentation.util

sealed class Screen(val route: String) {
    object TrainingsScreen : Screen("trainings_screen")
    object AddEditTrainingScreen : Screen("add_edit_training_screen")
    object ExercisesScreen : Screen("exercises_screen")
    object AddEditExerciseScreen : Screen("add_edit_exercise_screen")
}