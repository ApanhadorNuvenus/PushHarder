package com.example.apptest.feature_train.presentation.trainings

import com.example.apptest.feature_train.domain.model.Training
import com.example.apptest.feature_train.domain.util.OrderType
import com.example.apptest.feature_train.domain.util.TrainingOrder

data class TrainingsState(
    val trainings: List<Training> = emptyList(),
    val trainingOrder: TrainingOrder = TrainingOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)