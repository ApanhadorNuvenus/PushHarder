package com.example.apptest.feature_train.presentation.exercises

import com.example.apptest.feature_train.domain.model.Exercise
import com.example.apptest.feature_train.domain.util.ExerciseOrder
import com.example.apptest.feature_train.domain.util.OrderType

data class ExercisesState(
    val exercises: List<Exercise> = emptyList(),
    val exerciseOrder: ExerciseOrder = ExerciseOrder.Name(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)