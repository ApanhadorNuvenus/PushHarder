package com.crashapss.pushharder.feature_train.presentation.add_edit_exercises

import androidx.compose.ui.focus.FocusState

sealed class AddEditExercisesEvent {
    data class EnteredTitle(val value: String) : AddEditExercisesEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : AddEditExercisesEvent()
    data class EnteredDescription(val value: String) : AddEditExercisesEvent()
    data class ChangeGoalFocus(val focusState: FocusState) : AddEditExercisesEvent()
    data class EnteredGoal(val value: String) : AddEditExercisesEvent()
    data class ChangeDescriptionFocus(val focusState: FocusState) : AddEditExercisesEvent()
    object SaveExercise : AddEditExercisesEvent()
}