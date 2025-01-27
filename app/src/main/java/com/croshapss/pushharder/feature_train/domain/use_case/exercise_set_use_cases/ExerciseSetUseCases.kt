package com.croshapss.pushharder.feature_train.domain.use_case.exercise_set_use_cases

data class ExerciseSetUseCases(
    val getSetsForTrainingExercise: GetSetsForTrainingExercise,
    val addExerciseSet: AddExerciseSet,
    val deleteExerciseSet: DeleteExerciseSet
)