package com.example.apptest.feature_train.presentation.add_edit_exercise

import androidx.compose.ui.focus.FocusState
import com.example.apptest.feature_train.domain.model.ExerciseType

sealed class AddEditExercisesEvent {
    data class EnteredTitle(val value: String) : AddEditExercisesEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : AddEditExercisesEvent()
    data class EnteredDescription(val value: String) : AddEditExercisesEvent()
    data class ChangeGoalFocus(val focusState: FocusState) : AddEditExercisesEvent()
    data class EnteredGoal(val value: String) : AddEditExercisesEvent()
    data class ChangeDescriptionFocus(val focusState: FocusState) : AddEditExercisesEvent()
    object SaveExercise : AddEditExercisesEvent()
}