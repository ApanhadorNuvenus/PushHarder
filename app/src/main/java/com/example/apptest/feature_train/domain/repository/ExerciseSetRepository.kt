package com.example.apptest.feature_train.domain.repository

import com.example.apptest.feature_train.domain.model.ExerciseSet
import kotlinx.coroutines.flow.Flow

interface ExerciseSetRepository {
    suspend fun addSet(set: ExerciseSet)

    fun getSetsForTrainingExercise(teID: String): Flow<List<ExerciseSet>>

    suspend fun deleteExerciseSet(set: ExerciseSet)
}