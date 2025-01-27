package com.croshapss.pushharder.feature_train.domain.use_case.trainingExercise_use_case

import com.croshapss.pushharder.feature_train.domain.model.TrainingExercise
import com.croshapss.pushharder.feature_train.domain.repository.TrainingExerciseRepository

class DeleteTrainingExercise(
    private val repository: TrainingExerciseRepository
) {
    suspend operator fun invoke(trainingExercise: TrainingExercise) {
        repository.deleteTrainingExercise(trainingExercise)
    }
}