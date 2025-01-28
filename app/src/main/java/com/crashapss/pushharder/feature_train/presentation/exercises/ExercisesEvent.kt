package com.crashapss.pushharder.feature_train.presentation.exercises

import com.crashapss.pushharder.feature_train.domain.model.Exercise
import com.crashapss.pushharder.feature_train.domain.util.ExerciseOrder

sealed class ExercisesEvent {
    data class Order(val exerciseOrder: ExerciseOrder) : ExercisesEvent()
    data class DeleteExercise(val exercise: Exercise) : ExercisesEvent()
    object RestoreExercise : ExercisesEvent()
    object ToggleOrderSection : ExercisesEvent()
}