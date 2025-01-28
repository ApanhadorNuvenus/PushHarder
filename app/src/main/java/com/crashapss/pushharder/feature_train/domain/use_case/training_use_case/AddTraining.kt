package com.crashapss.pushharder.feature_train.domain.use_case.training_use_case

import com.crashapss.pushharder.feature_train.domain.model.InvalidTrainingException
import com.crashapss.pushharder.feature_train.domain.model.Training
import com.crashapss.pushharder.feature_train.domain.repository.TrainingRepository

class AddTraining(private val repository: TrainingRepository) {
    @Throws(InvalidTrainingException::class)
    suspend operator fun invoke(training: Training) {
        if (training.title.isBlank()) {
            throw InvalidTrainingException("Training title cannot be empty")
        }
        repository.addTraining(training)
    }
}