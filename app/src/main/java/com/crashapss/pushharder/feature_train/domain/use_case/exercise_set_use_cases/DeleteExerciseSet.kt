package com.crashapss.pushharder.feature_train.domain.use_case.exercise_set_use_cases

import com.crashapss.pushharder.feature_train.domain.model.ExerciseSet
import com.crashapss.pushharder.feature_train.domain.repository.ExerciseSetRepository

class DeleteExerciseSet(
    private val repository: ExerciseSetRepository
) {
    suspend operator fun invoke(exerciseSet: ExerciseSet) {
        repository.deleteExerciseSet(exerciseSet)
    }
}