package com.example.apptest.feature_train.presentation.trainingExercises

import com.example.apptest.feature_train.domain.model.Exercise
import com.example.apptest.feature_train.domain.model.ExerciseSet
import com.example.apptest.feature_train.domain.model.TrainingExercise

data class TrainingExerciseWithSets(
    val trainingExercise: TrainingExercise,
    val exercise: Exercise?, // Can be nullable in case it is still loading or doesn't exist
    val sets: List<ExerciseSet> = emptyList(),
    var currentWeightInput: String = "", // ADDED: For temporary UI input
    var currentFailureState: Boolean = false // ADDED: For temporary UI state
)