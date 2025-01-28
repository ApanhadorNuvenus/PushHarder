package com.crashapss.pushharder.feature_train.domain.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}