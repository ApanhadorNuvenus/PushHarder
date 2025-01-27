package com.croshapss.pushharder.feature_train.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = TrainingExercise::class,
            parentColumns = ["id"],
            childColumns = ["trainingExerciseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("trainingExerciseId")]
)
data class ExerciseSet(
    val trainingExerciseId: String,
    val setNumber: Int,
    val reps: Int?,
    @PrimaryKey val id: String = UUID.randomUUID().toString()
)