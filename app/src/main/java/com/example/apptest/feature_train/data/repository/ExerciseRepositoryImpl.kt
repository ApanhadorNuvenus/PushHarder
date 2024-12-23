package com.example.apptest.feature_train.data.repository

import com.example.apptest.feature_train.data.data_source.ExerciseDao
import com.example.apptest.feature_train.data.data_source.TrainingDao
import com.example.apptest.feature_train.domain.model.Exercise
import com.example.apptest.feature_train.domain.model.Training
import com.example.apptest.feature_train.domain.model.TrainingExercise
import com.example.apptest.feature_train.domain.repository.ExerciseRepository
import com.example.apptest.feature_train.domain.repository.TrainingRepository
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
}