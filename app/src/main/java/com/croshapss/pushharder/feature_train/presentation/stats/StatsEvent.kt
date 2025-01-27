package com.croshapss.pushharder.feature_train.presentation.stats

sealed class StatsEvent {
    data class SelectExercise(val exerciseName: String) : StatsEvent()
}