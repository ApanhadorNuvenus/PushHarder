package com.crashapss.pushharder.feature_train.presentation.trainings

import com.crashapss.pushharder.feature_train.domain.model.Training
import com.crashapss.pushharder.feature_train.domain.util.TrainingOrder

sealed class TrainingsEvent {
    data class Order(val trainingOrder: TrainingOrder) : TrainingsEvent()
    data class DeleteTraining(val training: Training) : TrainingsEvent()
    object RestoreTraining : TrainingsEvent()
    object ToggleOrderSection : TrainingsEvent()
}