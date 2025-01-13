package com.example.apptest.feature_train.domain.use_case.set

import com.example.apptest.feature_train.domain.model.ExerciseSet
import com.example.apptest.feature_train.domain.repository.ExerciseSetRepository

class DeleteExerciseSet(
    private val repository: ExerciseSetRepository
) {
    suspend operator fun invoke(set: ExerciseSet) {
        repository.deleteExerciseSet(set)
    }
}