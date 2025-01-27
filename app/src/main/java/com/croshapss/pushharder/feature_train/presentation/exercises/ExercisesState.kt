package com.croshapss.pushharder.feature_train.presentation.exercises

import com.croshapss.pushharder.feature_train.domain.model.Exercise
import com.croshapss.pushharder.feature_train.domain.util.ExerciseOrder
import com.croshapss.pushharder.feature_train.domain.util.OrderType

data class ExercisesState(
    val exercises: List<Exercise> = emptyList(),
    val exerciseOrder: ExerciseOrder = ExerciseOrder.Name(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)