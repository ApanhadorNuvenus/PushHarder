package com.croshapss.pushharder.feature_train.presentation.stats

import com.croshapss.pushharder.feature_train.domain.model.Exercise
import com.croshapss.pushharder.feature_train.domain.model.Training
import com.croshapss.pushharder.feature_train.presentation.trainingExercises.TrainingExerciseWithSets

data class StatsState(
    val allExercises: List<Exercise> = emptyList(),
    val selectedExerciseName: String? = null,
    val filteredTrainings: List<Training> = emptyList(),
    val trainingExercisesWithSets: Map<String, List<TrainingExerciseWithSets>> = emptyMap() // Training ID to list of exercises with sets
)