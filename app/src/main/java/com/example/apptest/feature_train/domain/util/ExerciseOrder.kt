package com.example.apptest.feature_train.domain.util

sealed class ExerciseOrder(val orderType: OrderType) {
    class Name(orderType: OrderType) : ExerciseOrder(orderType)
//    class Type(orderType: OrderType) : ExerciseOrder(orderType)

    fun copy(orderType: OrderType): ExerciseOrder {
        return when (this) {
            is Name -> Name(orderType)
//            is Type -> Type(orderType)
        }
    }
}