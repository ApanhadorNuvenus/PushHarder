package com.crashapss.pushharder.feature_train.presentation.trainingExercises

import com.crashapss.pushharder.feature_train.domain.model.Exercise
import com.crashapss.pushharder.feature_train.domain.model.TrainingExercise

sealed class TrainingExercisesEvent {
    data class DeleteTrainingExercise(val trainingExercise: TrainingExercise) : TrainingExercisesEvent()
    object RestoreExerciseTraining : TrainingExercisesEvent()
    data class AddTrainingExercise(
        val trainingId: String,
        val exercise: Exercise,
        val failure: Boolean?,
        val weights: Float?
    ) : TrainingExercisesEvent()
}