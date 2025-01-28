package com.crashapss.pushharder.feature_train.presentation.trainings

import com.crashapss.pushharder.feature_train.domain.model.Training
import com.crashapss.pushharder.feature_train.domain.util.OrderType
import com.crashapss.pushharder.feature_train.domain.util.TrainingOrder

data class TrainingsState(
    val trainings: List<Training> = emptyList(),
    val trainingOrder: TrainingOrder = TrainingOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)