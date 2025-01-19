package com.example.apptest.feature_train.domain.repository

import com.example.apptest.feature_train.domain.model.Exercise
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {

    fun getAllExercises(): Flow<List<Exercise>>

    fun getExerciseById(exerciseId: Int): Flow<Exercise?>

    suspend fun addExercise(exercise: Exercise)

    suspend fun deleteExercise(exercise: Exercise)

    suspend fun updateExercise(exercise: Exercise)
}