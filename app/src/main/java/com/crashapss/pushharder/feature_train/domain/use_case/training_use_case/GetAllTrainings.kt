package com.crashapss.pushharder.feature_train.domain.use_case.training_use_case

import com.crashapss.pushharder.feature_train.domain.model.Training
import com.crashapss.pushharder.feature_train.domain.repository.TrainingRepository
import com.crashapss.pushharder.feature_train.domain.util.OrderType
import com.crashapss.pushharder.feature_train.domain.util.TrainingOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllTrainings(
    private val repository: TrainingRepository
) {
    operator fun invoke(
        trainingOrder: TrainingOrder = TrainingOrder.Date(OrderType.Descending)
    ): Flow<List<Training>> {
        return repository.getAllTrainings().map { trainings ->
            when (trainingOrder.orderType) {
                is OrderType.Ascending -> {
                    when (trainingOrder) {
                        is TrainingOrder.Title -> trainings.sortedBy { it.title.lowercase() }
                        is TrainingOrder.Date -> trainings.sortedBy { it.date }
                    }
                }

                is OrderType.Descending -> {
                    when (trainingOrder) {
                        is TrainingOrder.Title -> trainings.sortedByDescending { it.title.lowercase() }
                        is TrainingOrder.Date -> trainings.sortedByDescending { it.date }
                    }
                }
            }
        }
    }
}