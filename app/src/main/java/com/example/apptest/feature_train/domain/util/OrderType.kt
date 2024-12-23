package com.example.apptest.feature_train.domain.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}