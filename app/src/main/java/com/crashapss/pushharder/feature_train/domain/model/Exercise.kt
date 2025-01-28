package com.crashapss.pushharder.feature_train.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Exercise(
    val name: String,
    val description: String? = null,
    val goal: Int? = null,
    @PrimaryKey val id: Int? = null
)

class InvalidExerciseException(message: String) : Exception(message)