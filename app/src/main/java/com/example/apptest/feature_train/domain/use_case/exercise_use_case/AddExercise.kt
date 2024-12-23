package com.example.apptest.feature_train.domain.use_case.exercise_use_case

import com.example.apptest.feature_train.domain.model.Exercise
import com.example.apptest.feature_train.domain.model.InvalidExerciseException
import com.example.apptest.feature_train.domain.repository.ExerciseRepository

class AddExercise(private val repository: ExerciseRepository) {
    @Throws(InvalidExerciseException::class)
    suspend operator fun invoke(exercise: Exercise) {
        if (exercise.name.isBlank()) {
            throw InvalidExerciseException("Exercise name cannot be empty")
        }
        repository.addExercise(exercise)
    }
}