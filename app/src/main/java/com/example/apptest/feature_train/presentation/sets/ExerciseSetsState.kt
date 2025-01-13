package com.example.apptest.feature_train.presentation.sets

import com.example.apptest.feature_train.domain.model.ExerciseSet

data class ExerciseSetsState (
    val set: List<ExerciseSet> = emptyList()
)