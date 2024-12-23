package com.example.apptest.feature_train.domain.use_case.training_use_case

import com.example.apptest.feature_train.domain.model.Training
import com.example.apptest.feature_train.domain.repository.TrainingRepository

class GetTrainingById(private val repository: TrainingRepository) {
    suspend operator fun invoke(trainingId: String): Training? {
        return repository.getTrainingById(trainingId)
    }
}