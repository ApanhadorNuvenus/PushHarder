package com.example.apptest.feature_train.presentation.add_edit_training

import androidx.compose.ui.focus.FocusState
import com.example.apptest.feature_train.domain.model.Exercise
import com.example.apptest.feature_train.domain.model.TrainingExercise

sealed class AddEditTrainingsEvent {
    data class EnteredTitle(val value: String) : AddEditTrainingsEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : AddEditTrainingsEvent()
    data class EnteredWeight(val value: String) : AddEditTrainingsEvent()
    data class ChangeWeightFocus(val focusState: FocusState) : AddEditTrainingsEvent()
    data class ChangeFailureState(val value: Boolean) : AddEditTrainingsEvent()
    object SaveTraining : AddEditTrainingsEvent()
    data class AddExercise(
        val exercise: Exercise,
        val reps: Int?,
        val duration: Int?
    ) : AddEditTrainingsEvent()
    data class DeleteTemporaryExercise(val trainingExercise: TrainingExercise) : AddEditTrainingsEvent()
}