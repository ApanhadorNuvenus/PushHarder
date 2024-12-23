package com.example.apptest.feature_train.domain.use_case.exercise_use_case

import com.example.apptest.feature_train.domain.model.Exercise
import com.example.apptest.feature_train.domain.repository.ExerciseRepository
import com.example.apptest.feature_train.domain.util.ExerciseOrder
import com.example.apptest.feature_train.domain.util.OrderType
import kotlinx.coroutines.flow.Flow

class GetAllExercises(
    private val repository: ExerciseRepository
) {
    operator fun invoke(
        exerciseOrder: ExerciseOrder = ExerciseOrder.Name(OrderType.Descending)
    ): Flow<List<Exercise>> {
        return repository.getAllExercises()
    }
}