package com.croshapss.pushharder.feature_train.domain.model

sealed class ExerciseType {
    object Reps : ExerciseType()
    object Duration : ExerciseType()
}