package com.croshapss.pushharder.feature_train.presentation.exerciseSets

import com.croshapss.pushharder.feature_train.domain.model.ExerciseSet

data class ExerciseSetsState(
    val sets: List<ExerciseSet> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)