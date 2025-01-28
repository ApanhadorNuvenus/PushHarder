package com.crashapss.pushharder.feature_train.domain.use_case.exercise_set_use_cases

import com.crashapss.pushharder.feature_train.domain.model.ExerciseSet
import com.crashapss.pushharder.feature_train.domain.repository.ExerciseSetRepository
import kotlinx.coroutines.flow.Flow

class GetSetsForTrainingExercise(
    private val repository: ExerciseSetRepository
) {
    operator fun invoke(trainingExerciseId: String): Flow<List<ExerciseSet>> {
        return repository.getSetsForTrainingExercise(trainingExerciseId)
    }
}