package com.example.apptest.feature_train.domain.use_case.trainingExercise_use_case

import com.example.apptest.feature_train.domain.model.TrainingExercise
import com.example.apptest.feature_train.domain.repository.TrainingExerciseRepository
import kotlinx.coroutines.flow.Flow

class GetExercisesForTraining(
    private val repository: TrainingExerciseRepository
) {
    operator fun invoke(trainingId: String): Flow<List<TrainingExercise>> { // Parameter type changed to String
        return repository.getExercisesForTraining(trainingId)
    }
}