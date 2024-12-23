package com.example.apptest.feature_train.presentation.trainingExercises

import com.example.apptest.feature_train.domain.model.TrainingExercise

data class TrainingExercisesState(
    val trainingExercises: List<TrainingExercise> = emptyList()
)