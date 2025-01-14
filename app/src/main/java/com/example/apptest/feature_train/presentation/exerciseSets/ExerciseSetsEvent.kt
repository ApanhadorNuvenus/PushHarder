package com.example.apptest.feature_train.presentation.exerciseSets

import com.example.apptest.feature_train.domain.model.ExerciseSet

sealed class ExerciseSetsEvent {
    data class AddSet(val trainingExerciseId: String, val reps: Int) : ExerciseSetsEvent()
    data class DeleteSet(val exerciseSet: ExerciseSet) : ExerciseSetsEvent()
    data class LoadSets(val trainingExerciseId: String) : ExerciseSetsEvent()
}