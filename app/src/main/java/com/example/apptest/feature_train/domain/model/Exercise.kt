package com.example.apptest.feature_train.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Exercise(
    val name: String,
    val description: String? = null,
    @PrimaryKey val id: Int? = null
)

class InvalidExerciseException(message: String) : Exception(message)