package com.crashapss.pushharder.feature_train.presentation.exercises

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crashapss.pushharder.feature_train.domain.model.Exercise
import com.crashapss.pushharder.feature_train.domain.use_case.exercise_use_case.ExerciseUseCases
import com.crashapss.pushharder.feature_train.domain.util.ExerciseOrder
import com.crashapss.pushharder.feature_train.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class ExercisesViewModel @Inject constructor(
    private val exerciseUseCases: ExerciseUseCases
) : ViewModel() {
    private val _state = mutableStateOf(ExercisesState())
    val state: State<ExercisesState> = _state

    private var recentlyDeletedExercise: Exercise? = null
    private var getExercisesJob: Job? = null

    private val _pendingDeletionExercises = MutableStateFlow<List<Exercise>>(emptyList())
    val pendingDeletionExercises: StateFlow<List<Exercise>> = _pendingDeletionExercises

    private val _exerciseUpdateEvent = MutableSharedFlow<Unit>()
    val exerciseUpdateEvent = _exerciseUpdateEvent.asSharedFlow()

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
                    // Instead of deleting immediately, add to pending list
                    _pendingDeletionExercises.value = _pendingDeletionExercises.value + event.exercise
                }
            }

            is ExercisesEvent.RestoreExercise -> {
                viewModelScope.launch {
                    _pendingDeletionExercises.value.find { it.id == recentlyDeletedExercise?.id }?.let { exercise ->
                        _pendingDeletionExercises.value = _pendingDeletionExercises.value - exercise
                    }
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

    fun confirmDeletion() {
        viewModelScope.launch {
            _pendingDeletionExercises.value.forEach { exercise ->
                exerciseUseCases.deleteExercise(exercise)
            }
            _pendingDeletionExercises.value = emptyList()
        }
    }

    fun handlePendingDeletions() {
        viewModelScope.launch {
            _pendingDeletionExercises.value.forEach { exercise ->
                exerciseUseCases.deleteExercise(exercise)
                Log.d("ExerciseDelete", "delete on screen exit")
            }
            _pendingDeletionExercises.value = emptyList()
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

    fun emitExerciseUpdated() {
        viewModelScope.launch {
            _exerciseUpdateEvent.emit(Unit)
        }
    }
}