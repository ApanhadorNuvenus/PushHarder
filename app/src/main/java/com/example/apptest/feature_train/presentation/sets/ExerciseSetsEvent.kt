package com.example.apptest.feature_train.presentation.sets

import com.example.apptest.feature_train.domain.model.ExerciseSet

sealed class ExerciseSetsEvent {
    data class deleteExerciseSet(val set: ExerciseSet): ExerciseSetsEvent()
    data class addExerciseSet(
        val teID: String,
        val sets: Int?,
        val reps: Int?
    ): ExerciseSetsEvent()
}