package com.crashapss.pushharder.feature_train.domain.repository

import com.crashapss.pushharder.feature_train.domain.model.Training
import kotlinx.coroutines.flow.Flow

interface TrainingRepository {

    fun getAllTrainings(): Flow<List<Training>>

    suspend fun getTrainingById(trainingId: String): Training? // Changed parameter type to String

    suspend fun addTraining(training: Training)

    suspend fun deleteTraining(training: Training)

    suspend fun updateTraining(training: Training)
}