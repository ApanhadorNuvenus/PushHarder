package com.example.apptest.feature_train.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    foreignKeys = [
        ForeignKey(entity = Training::class, parentColumns = ["id"], childColumns = ["trainingId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Exercise::class, parentColumns = ["id"], childColumns = ["exerciseId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("exerciseId"), Index("trainingId")]
)
data class TrainingExercise(
    val trainingId: String,
    val exerciseId: Int,
//    reps and sets are moved to sets feature
//    val reps: Int? = null,
//    val sets: Int? = null,
    val failure: Boolean? = null, // Added nullable failure field
    @PrimaryKey val id: String = UUID.randomUUID().toString()
)