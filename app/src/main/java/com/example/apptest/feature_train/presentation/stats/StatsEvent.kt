package com.example.apptest.feature_train.presentation.stats

import com.example.apptest.feature_train.domain.model.Exercise

sealed class StatsEvent {
    data class SelectExercise(val exerciseName: String) : StatsEvent()
}