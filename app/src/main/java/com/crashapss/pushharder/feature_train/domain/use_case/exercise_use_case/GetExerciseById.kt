package com.crashapss.pushharder.feature_train.domain.use_case.exercise_use_case

import com.crashapss.pushharder.feature_train.domain.model.Exercise
import com.crashapss.pushharder.feature_train.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow

class GetExerciseById(private val repository: ExerciseRepository) {
    operator fun invoke(exerciseId: Int): Flow<Exercise?> {
        return repository.getExerciseById(exerciseId)
    }
}