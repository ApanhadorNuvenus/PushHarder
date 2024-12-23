package com.example.apptest.feature_train.data

import androidx.room.TypeConverter
import com.example.apptest.feature_train.domain.model.ExerciseType

class Converters {
    @TypeConverter
    fun fromExerciseType(exerciseType: ExerciseType): String {
        return when (exerciseType) {
            is ExerciseType.Reps -> "Reps"
            is ExerciseType.Duration -> "Duration"
        }
    }

    @TypeConverter
    fun toExerciseType(exerciseType: String): ExerciseType {
        return when (exerciseType) {
            "Reps" -> ExerciseType.Reps
            "Duration" -> ExerciseType.Duration
            else -> throw IllegalArgumentException("Unknown ExerciseType")
        }
    }
}