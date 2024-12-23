package com.example.apptest.feature_train.domain.use_case.trainingExercise_use_case

data class TrainingExerciseUseCases(
    val addTrainingExercise: AddTrainingExercise,
    val deleteTrainingExercise: DeleteTrainingExercise,
    val getExercisesForTraining: GetExercisesForTraining
)