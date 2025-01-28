package com.crashapss.pushharder.feature_train.presentation.add_edit_training

import androidx.compose.ui.focus.FocusState
import com.crashapss.pushharder.feature_train.domain.model.Exercise

sealed class AddEditTrainingsEvent {
    data class EnteredTitle(val value: String) : AddEditTrainingsEvent()
    data class ChangeTitleFocus(val focusState: FocusState) : AddEditTrainingsEvent()
    object SaveTraining : AddEditTrainingsEvent()
    data class AddExercise(val exercise: Exercise) : AddEditTrainingsEvent()
    data class AddSetToExercise(val trainingExerciseId: String, val setNumber: Int, val reps: Int) : AddEditTrainingsEvent()
    data class DeleteSetFromExercise(val trainingExerciseId: String, val setId: String) : AddEditTrainingsEvent()
    data class DeleteTrainingExercise(val trainingExerciseId: String) : AddEditTrainingsEvent()

    // --- ADD THESE NEW EVENTS ---
    data class EnteredExerciseWeight(val trainingExerciseId: String, val value: String) : AddEditTrainingsEvent()
    data class ChangeExerciseFailureState(val trainingExerciseId: String, val value: Boolean) : AddEditTrainingsEvent()
}