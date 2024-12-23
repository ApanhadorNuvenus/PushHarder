package com.example.apptest.feature_train.domain.use_case.exercise_use_case

import com.example.apptest.feature_train.domain.model.Exercise
import com.example.apptest.feature_train.domain.repository.ExerciseRepository
import com.example.apptest.feature_train.domain.util.ExerciseOrder
import com.example.apptest.feature_train.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetExercises(private val repository: ExerciseRepository) {
    operator fun invoke(
        exerciseOrder: ExerciseOrder = ExerciseOrder.Name(OrderType.Descending)
    ): Flow<List<Exercise>> {
        return repository.getAllExercises().map { exercises ->
            when (exerciseOrder.orderType) {
                is OrderType.Ascending -> {
                    when (exerciseOrder) {
                        is ExerciseOrder.Name -> exercises.sortedBy { it.name.lowercase() }
                        is ExerciseOrder.Type -> exercises.sortedBy { it.exerciseType.toString() }
                    }
                }

                is OrderType.Descending -> {
                    when (exerciseOrder) {
                        is ExerciseOrder.Name -> exercises.sortedByDescending { it.name.lowercase() }
                        is ExerciseOrder.Type -> exercises.sortedByDescending { it.exerciseType.toString() }
                    }
                }
            }
        }
    }
}