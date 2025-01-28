package com.crashapss.pushharder.feature_train.domain.use_case.training_use_case

import com.crashapss.pushharder.feature_train.domain.model.Training
import com.crashapss.pushharder.feature_train.domain.repository.TrainingRepository

class UpdateTraining(private val repository: TrainingRepository) {
    suspend operator fun invoke(training: Training) {
        repository.updateTraining(training)
    }
}