package com.example.apptest.feature_train.presentation.stats

sealed class StatsEvent {
    data class SelectExercise(val exerciseName: String) : StatsEvent()
}