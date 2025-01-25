package com.example.apptest.feature_train.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Training(
    val title: String,
    val date: Long,
    @PrimaryKey val id: String = UUID.randomUUID().toString() // Now a UUID string
)

class InvalidTrainingException(message: String) : Exception(message)