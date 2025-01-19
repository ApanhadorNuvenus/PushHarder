package com.example.apptest.feature_train.domain.use_case.exercise_use_case

data class ExerciseUseCases(
    val getAllExercises: GetExercises,
    val deleteExercise: DeleteExercise,
    val addExercise: AddExercise,
    val getExerciseById: GetExerciseById,
    val updateExercise: UpdateExercise
)