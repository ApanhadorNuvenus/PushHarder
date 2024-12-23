package com.example.apptest.feature_train.domain.use_case.trainingExercise_use_case

import com.example.apptest.feature_train.domain.model.TrainingExercise
import com.example.apptest.feature_train.domain.repository.TrainingExerciseRepository

class AddTrainingExercise(
    private val repository: TrainingExerciseRepository
) {
    suspend operator fun invoke(trainingExercise: TrainingExercise) {
        repository.addTrainingExercise(trainingExercise)
    }
}