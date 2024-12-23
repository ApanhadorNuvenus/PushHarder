package com.example.apptest.feature_train.data.data_source

import androidx.room.TypeConverter
import com.example.apptest.feature_train.domain.model.ExerciseType

class ExerciseTypeConverter {

    @TypeConverter
    fun fromExerciseType(exerciseType: ExerciseType): String {
        return when (exerciseType) {
            is ExerciseType.Reps -> "Reps"
            is ExerciseType.Duration -> "Duration"
        }
    }

    @TypeConverter
    fun toExerciseType(value: String): ExerciseType {
        return when (value) {
            "Reps" -> ExerciseType.Reps
            "Duration" -> ExerciseType.Duration
            else -> throw IllegalArgumentException("Unknown ExerciseType")
        }
    }
}