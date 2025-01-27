package com.croshapss.pushharder.feature_train.data.repository

import com.croshapss.pushharder.feature_train.data.data_source.ExerciseDao
import com.croshapss.pushharder.feature_train.domain.model.Exercise
import com.croshapss.pushharder.feature_train.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow

class ExerciseRepositoryImpl(
    private val dao: ExerciseDao
) : ExerciseRepository {
    override fun getAllExercises(): Flow<List<Exercise>> {
        return dao.getAllExercises()
    }

    override fun getExerciseById(exerciseId: Int): Flow<Exercise?> {
        return dao.getExerciseById(exerciseId)
    }

    override suspend fun addExercise(exercise: Exercise) {
        dao.addExercise(exercise)
    }

    override suspend fun deleteExercise(exercise: Exercise) {
        dao.deleteExercise(exercise)
    }

    override suspend fun updateExercise(exercise: Exercise) {
        dao.updateExercise(exercise)
    }
}