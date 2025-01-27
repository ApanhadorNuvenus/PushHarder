package com.croshapss.pushharder.feature_train.presentation.trainings

import com.croshapss.pushharder.feature_train.domain.model.Training
import com.croshapss.pushharder.feature_train.domain.util.OrderType
import com.croshapss.pushharder.feature_train.domain.util.TrainingOrder

data class TrainingsState(
    val trainings: List<Training> = emptyList(),
    val trainingOrder: TrainingOrder = TrainingOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)