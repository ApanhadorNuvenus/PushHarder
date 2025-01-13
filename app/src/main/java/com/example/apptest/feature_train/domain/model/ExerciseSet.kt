package com.example.apptest.feature_train.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    foreignKeys = [
        ForeignKey(entity = TrainingExercise::class, parentColumns = ["id"], childColumns = ["teId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("teId")]
)
data class ExerciseSet(
    val teId: String,
    val sets: Int? = null,
    val reps: Int? = null,
    @PrimaryKey val id: String = UUID.randomUUID().toString()
)