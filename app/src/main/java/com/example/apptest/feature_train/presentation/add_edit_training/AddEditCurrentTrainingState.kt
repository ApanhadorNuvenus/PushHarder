package com.example.apptest.feature_train.presentation.add_edit_training

import com.example.apptest.feature_train.domain.model.ExerciseSet
import com.example.apptest.feature_train.domain.model.TrainingExercise

data class AddEditCurrentTrainingState(
    val title: String = "",
    val date: Long = 0L,
    val failure: Boolean = false,
    val weights: String? = null,
    val trainingId: String? = null, // Make trainingId nullable initially
    val isTitleHintVisible: Boolean = true,
    val isWeightHintVisible: Boolean = true,
    val temporaryExercisesWithSets: MutableList<TemporaryTrainingExerciseWithSets> = mutableListOf()
)

data class TemporaryTrainingExerciseWithSets(
    val trainingExercise: TrainingExercise,
    val sets: MutableList<ExerciseSet> = mutableListOf()
)