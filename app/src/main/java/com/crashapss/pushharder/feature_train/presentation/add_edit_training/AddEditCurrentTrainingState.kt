package com.crashapss.pushharder.feature_train.presentation.add_edit_training

import com.crashapss.pushharder.feature_train.domain.model.ExerciseSet
import com.crashapss.pushharder.feature_train.domain.model.TrainingExercise

data class AddEditCurrentTrainingState(
    val title: String = "",
    val date: Long = 0L,
    val trainingId: String? = null,
    val isTitleHintVisible: Boolean = true,

    val temporaryExercisesWithSets: MutableList<TemporaryTrainingExerciseWithSets> = mutableListOf()
)

data class TemporaryTrainingExerciseWithSets(
    val trainingExercise: TrainingExercise,
    val sets: MutableList<ExerciseSet> = mutableListOf()
)