package com.example.apptest.feature_train.domain.repository

import com.example.apptest.feature_train.domain.model.ExerciseSet
import kotlinx.coroutines.flow.Flow

interface ExerciseSetRepository {
    fun getSetsForTrainingExercise(trainingExerciseId: String): Flow<List<ExerciseSet>>

    suspend fun addExerciseSet(exerciseSet: ExerciseSet)

    suspend fun deleteExerciseSet(exerciseSet: ExerciseSet)
}