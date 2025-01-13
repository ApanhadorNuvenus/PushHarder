package com.example.apptest.feature_train.data.repository

import com.example.apptest.feature_train.data.data_source.ExerciseSetDao
import com.example.apptest.feature_train.domain.model.ExerciseSet
import com.example.apptest.feature_train.domain.model.TrainingExercise
import com.example.apptest.feature_train.domain.repository.ExerciseSetRepository
import com.example.apptest.feature_train.domain.repository.TrainingExerciseRepository
import kotlinx.coroutines.flow.Flow

class ExerciseSetRepositoryImpl(
    private val dao: ExerciseSetDao
) : ExerciseSetRepository {

    override suspend fun addSet(set: ExerciseSet) {
        dao.addExerciseSet(set)
    }

    override fun getSetsForTrainingExercise(teID: String): Flow<List<ExerciseSet>> {
        return dao.getExerciseSetsForTE(teID)
    }

    override suspend fun deleteExerciseSet(set: ExerciseSet) {
        dao.deleteExerciseSet(set)
    }
}