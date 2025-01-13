package com.example.apptest.feature_train.domain.use_case.set

import com.example.apptest.feature_train.domain.model.ExerciseSet
import com.example.apptest.feature_train.domain.repository.ExerciseSetRepository
import kotlinx.coroutines.flow.Flow


class GetExerciseSetsForTE(
    private val repository: ExerciseSetRepository
) {
    operator fun invoke(teId: String): Flow<List<ExerciseSet>> {
        return repository.getSetsForTrainingExercise(teId)
    }
}