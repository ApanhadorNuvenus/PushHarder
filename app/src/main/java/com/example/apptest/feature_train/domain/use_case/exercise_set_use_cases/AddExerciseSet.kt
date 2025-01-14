package com.example.apptest.feature_train.domain.use_case.exercise_set_use_cases

import com.example.apptest.feature_train.domain.model.ExerciseSet
import com.example.apptest.feature_train.domain.repository.ExerciseSetRepository

class AddExerciseSet(
    private val repository: ExerciseSetRepository
) {
    suspend operator fun invoke(exerciseSet: ExerciseSet) {
        if (exerciseSet.setNumber <= 0) {
            throw IllegalArgumentException("Set number must be greater than 0")
        }
        if (exerciseSet.reps != null && exerciseSet.reps <= 0) {
            throw IllegalArgumentException("Repetitions must be greater than 0")
        }
        repository.addExerciseSet(exerciseSet)
    }
}