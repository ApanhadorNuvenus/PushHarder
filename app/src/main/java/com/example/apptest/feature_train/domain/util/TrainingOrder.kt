package com.example.apptest.feature_train.domain.util

sealed class TrainingOrder(val orderType: OrderType) {
    class Title(orderType: OrderType) : TrainingOrder(orderType)
    class Date(orderType: OrderType) : TrainingOrder(orderType)

    fun copy(orderType: OrderType): TrainingOrder {
        return when (this) {
            is Title -> Title(orderType)
            is Date -> Date(orderType)
        }
    }
}