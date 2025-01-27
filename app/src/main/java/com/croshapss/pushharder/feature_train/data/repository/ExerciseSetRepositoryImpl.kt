package com.croshapss.pushharder.feature_train.data.repository

import com.croshapss.pushharder.feature_train.data.data_source.ExerciseSetDao
import com.croshapss.pushharder.feature_train.domain.model.ExerciseSet
import com.croshapss.pushharder.feature_train.domain.repository.ExerciseSetRepository
import kotlinx.coroutines.flow.Flow

class ExerciseSetRepositoryImpl(
    private val dao: ExerciseSetDao
) : ExerciseSetRepository {

    override fun getSetsForTrainingExercise(trainingExerciseId: String): Flow<List<ExerciseSet>> {
        return dao.getSetsForTrainingExercise(trainingExerciseId)
    }

    override suspend fun addExerciseSet(exerciseSet: ExerciseSet) {
        dao.addExerciseSet(exerciseSet)
    }

    override suspend fun deleteExerciseSet(exerciseSet: ExerciseSet) {
        dao.deleteExerciseSet(exerciseSet)
    }
}