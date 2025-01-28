package com.crashapss.pushharder.feature_train.presentation.add_edit_exercises

data class AddEditCurrentExerciseState(
    val title: String = "",
    val description: String = "",
    val goal: Int? = null,
    val exerciseId: Int? = null,
    val isTitleHintVisible: Boolean = true,
    val isDescriptionHintVisible: Boolean = true,
    val isGoalHintVisible: Boolean = true
)