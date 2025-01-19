package com.example.apptest.feature_train.presentation.add_edit_exercises

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptest.feature_train.domain.model.Exercise
import com.example.apptest.feature_train.domain.model.InvalidExerciseException
import com.example.apptest.feature_train.domain.use_case.exercise_use_case.ExerciseUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

@HiltViewModel
class AddEditExercisesViewModel @Inject constructor(
    private val exerciseUseCases: ExerciseUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _exerciseState = mutableStateOf(AddEditCurrentExerciseState())
    val exerciseState: State<AddEditCurrentExerciseState> = _exerciseState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<Int>("exerciseId")?.let { exerciseId ->
            if (exerciseId != -1) {
                viewModelScope.launch {
                    exerciseUseCases.getExerciseById(exerciseId)
                        .filterNotNull()
                        .first()
                        .let { exercise ->
                            _exerciseState.value = exerciseState.value.copy(
                                title = exercise.name,
                                goal = exercise.goal,
                                description = exercise.description ?: "",
                                exerciseId = exercise.id,
                                isTitleHintVisible = false,
                                isDescriptionHintVisible = exercise.description.isNullOrBlank(),
                                isGoalHintVisible = exercise.goal == null
                            )
                        }
                }
            } else {
                _exerciseState.value = AddEditCurrentExerciseState(
                    title = "",
                    description = "",
                    goal = null,
                    isTitleHintVisible = true,
                    isDescriptionHintVisible = true,
                    isGoalHintVisible = true
                )
            }
        }
    }

    fun onEvent(event: AddEditExercisesEvent) {
        when (event) {
            is AddEditExercisesEvent.EnteredTitle -> {
                _exerciseState.value = exerciseState.value.copy(
                    title = event.value,
                    isTitleHintVisible = event.value.isBlank()
                )
            }
            is AddEditExercisesEvent.ChangeTitleFocus -> {
                _exerciseState.value = exerciseState.value.copy(
                    isTitleHintVisible = !event.focusState.isFocused && exerciseState.value.title.isBlank()
                )
            }
            is AddEditExercisesEvent.EnteredDescription -> {
                _exerciseState.value = exerciseState.value.copy(
                    description = event.value,
                    isDescriptionHintVisible = event.value.isBlank()
                )
            }
            is AddEditExercisesEvent.ChangeDescriptionFocus -> {
                _exerciseState.value = exerciseState.value.copy(
                    isDescriptionHintVisible = !event.focusState.isFocused && exerciseState.value.description.isBlank()
                )
            }
            is AddEditExercisesEvent.EnteredGoal -> {
                _exerciseState.value = exerciseState.value.copy(
                    goal = event.value.toIntOrNull(),
                    isGoalHintVisible = event.value.isBlank()
                )
            }
            is AddEditExercisesEvent.ChangeGoalFocus -> {
                _exerciseState.value = exerciseState.value.copy(
                    isGoalHintVisible = !event.focusState.isFocused && exerciseState.value.goal == null
                )
            }
            is AddEditExercisesEvent.SaveExercise -> {
                viewModelScope.launch {
                    try {
                        exerciseUseCases.addExercise(
                            Exercise(
                                name = exerciseState.value.title,
                                description = exerciseState.value.description,
                                goal = exerciseState.value.goal,
                                id = exerciseState.value.exerciseId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveExercise)
                    } catch (e: InvalidExerciseException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save exercise"
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveExercise : UiEvent()
    }
}