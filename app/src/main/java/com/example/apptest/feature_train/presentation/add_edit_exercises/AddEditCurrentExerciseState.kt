package com.example.apptest.feature_train.presentation.add_edit_exercise

import com.example.apptest.feature_train.domain.model.ExerciseType

data class AddEditCurrentExerciseState(
    val title: String = "",
    val description: String = "",
    val goal: Int? = null,
    val exerciseId: Int? = null,
    val isTitleHintVisible: Boolean = true,
    val isDescriptionHintVisible: Boolean = true,
    val isGoalHintVisible: Boolean = true
)