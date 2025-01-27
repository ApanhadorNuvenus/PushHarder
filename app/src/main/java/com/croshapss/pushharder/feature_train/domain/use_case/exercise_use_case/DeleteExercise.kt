package com.croshapss.pushharder.feature_train.domain.use_case.exercise_use_case

import com.croshapss.pushharder.feature_train.domain.model.Exercise
import com.croshapss.pushharder.feature_train.domain.repository.ExerciseRepository

class DeleteExercise(private val repository: ExerciseRepository) {
    suspend operator fun invoke(exercise: Exercise) {
        repository.deleteExercise(exercise)
    }
}