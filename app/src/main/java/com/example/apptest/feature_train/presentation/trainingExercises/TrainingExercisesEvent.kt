package com.example.apptest.feature_train.presentation.trainingExercises

import com.example.apptest.feature_train.domain.model.Exercise
import com.example.apptest.feature_train.domain.model.TrainingExercise

sealed class TrainingExercisesEvent {
    data class DeleteTrainingExercise(val trainingExercise: TrainingExercise) : TrainingExercisesEvent()
    object RestoreExerciseTraining : TrainingExercisesEvent()
    data class AddExercise(
        val trainingId: String,
        val exercise: Exercise,
        val reps: Int?,
        val duration: Int?
    ) : TrainingExercisesEvent()
}