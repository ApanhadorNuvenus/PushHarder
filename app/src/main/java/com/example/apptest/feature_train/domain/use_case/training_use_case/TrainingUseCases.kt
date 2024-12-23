package com.example.apptest.feature_train.domain.use_case.training_use_case

data class TrainingUseCases(
    val getAllTrainings: GetAllTrainings,
    val deleteTraining: DeleteTraining,
    val addTraining: AddTraining,
    val getTrainingById: GetTrainingById,
    val updateTraining: UpdateTraining
)