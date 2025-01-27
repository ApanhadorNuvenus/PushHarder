package com.croshapss.pushharder.feature_train.data.repository

import com.croshapss.pushharder.feature_train.data.data_source.TrainingExerciseDao
import com.croshapss.pushharder.feature_train.domain.model.TrainingExercise
import com.croshapss.pushharder.feature_train.domain.repository.TrainingExerciseRepository
import kotlinx.coroutines.flow.Flow

class TrainingExerciseRepositoryImpl(
    private val dao: TrainingExerciseDao
) : TrainingExerciseRepository {
    override suspend fun addTrainingExercise(trainingExercise: TrainingExercise) {
        dao.addTrainingExercise(trainingExercise)
    }

    override fun getExercisesForTraining(trainingId: String): Flow<List<TrainingExercise>> { // Parameter type changed to String
        return dao.getExercisesForTraining(trainingId)
    }

    override suspend fun deleteTrainingExercise(trainingExercise: TrainingExercise) {
        dao.deleteTrainingExercise(trainingExercise)
    }
}