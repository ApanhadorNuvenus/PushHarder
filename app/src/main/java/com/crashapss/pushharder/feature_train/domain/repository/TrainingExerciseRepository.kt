package com.crashapss.pushharder.feature_train.domain.repository

import com.crashapss.pushharder.feature_train.domain.model.TrainingExercise
import kotlinx.coroutines.flow.Flow

interface TrainingExerciseRepository {
    suspend fun addTrainingExercise(trainingExercise: TrainingExercise)

    fun getExercisesForTraining(trainingId: String): Flow<List<TrainingExercise>> // Parameter type changed to String

    suspend fun deleteTrainingExercise(trainingExercise: TrainingExercise)
}