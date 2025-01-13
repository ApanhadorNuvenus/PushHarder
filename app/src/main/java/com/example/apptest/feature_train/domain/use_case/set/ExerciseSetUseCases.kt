package com.example.apptest.feature_train.domain.use_case.set


data class ExerciseSetUseCases(
    val addExerciseSet: AddExerciseSet,
    val deleteExerciseSet: DeleteExerciseSet,
    val getExerciseSetsForTE: GetExerciseSetsForTE
)