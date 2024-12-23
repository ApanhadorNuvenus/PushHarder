package com.example.apptest.feature_train.presentation.exercises

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptest.feature_train.domain.model.Exercise
import com.example.apptest.feature_train.domain.use_case.exercise_use_case.ExerciseUseCases
import com.example.apptest.feature_train.domain.util.ExerciseOrder
import com.example.apptest.feature_train.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExercisesViewModel @Inject constructor(
    private val exerciseUseCases: ExerciseUseCases
) : ViewModel() {
    private val _state = mutableStateOf(ExercisesState())
    val state: State<ExercisesState> = _state

    private var recentlyDeletedExercise: Exercise? = null
    private var getExercisesJob: Job? = null

    init {
        getExercises(ExerciseOrder.Name(OrderType.Descending))
    }

    fun onEvent(event: ExercisesEvent) {
        when (event) {
            is ExercisesEvent.Order -> {
                if (state.value.exerciseOrder::class == event.exerciseOrder::class &&
                    state.value.exerciseOrder.orderType == event.exerciseOrder.orderType
                ) {
                    return
                }
                getExercises(event.exerciseOrder)
            }

            is ExercisesEvent.DeleteExercise -> {
                viewModelScope.launch {
                    exerciseUseCases.deleteExercise(event.exercise)
                    recentlyDeletedExercise = event.exercise
                }
            }

            is ExercisesEvent.RestoreExercise -> {
                viewModelScope.launch {
                    exerciseUseCases.addExercise(recentlyDeletedExercise ?: return@launch)
                    recentlyDeletedExercise = null
                }
            }

            is ExercisesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getExercises(exerciseOrder: ExerciseOrder) {
        getExercisesJob?.cancel()
        getExercisesJob = exerciseUseCases.getAllExercises(exerciseOrder)
            .onEach { exercises ->
                _state.value = state.value.copy(
                    exercises = exercises,
                    exerciseOrder = exerciseOrder
                )
            }
            .launchIn(viewModelScope)
    }
}