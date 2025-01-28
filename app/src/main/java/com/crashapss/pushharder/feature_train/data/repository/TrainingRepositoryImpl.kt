package com.crashapss.pushharder.feature_train.data.repository

import com.crashapss.pushharder.feature_train.data.data_source.TrainingDao
import com.crashapss.pushharder.feature_train.domain.model.Training
import com.crashapss.pushharder.feature_train.domain.repository.TrainingRepository
import kotlinx.coroutines.flow.Flow

class TrainingRepositoryImpl(
    private val dao: TrainingDao
) : TrainingRepository {
    override fun getAllTrainings(): Flow<List<Training>> {
        return dao.getAllTrainings()
    }

    override suspend fun getTrainingById(trainingId: String): Training? {
        return dao.getTrainingById(trainingId)
    }

    override suspend fun addTraining(training: Training) {
        dao.addTraining(training)
    }

    override suspend fun deleteTraining(training: Training) {
        dao.deleteTraining(training)
    }

    override suspend fun updateTraining(training: Training) {
        dao.updateTraining(training)
    }
}