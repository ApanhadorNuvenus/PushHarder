package com.example.apptest.feature_train.presentation.exerciseSets

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptest.feature_train.domain.model.ExerciseSet
import com.example.apptest.feature_train.domain.use_case.exercise_set_use_cases.ExerciseSetUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


// In ExerciseSetsViewModel.kt
@HiltViewModel
class ExerciseSetsViewModel @Inject constructor(
    private val exerciseSetUseCases: ExerciseSetUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(ExerciseSetsState())
    val state: State<ExerciseSetsState> = _state

    private var getSetsJob: Job? = null

    fun onEvent(event: ExerciseSetsEvent) {
        when(event) {
            is ExerciseSetsEvent.LoadSets -> {
                loadSets(event.trainingExerciseId)
            }
            is ExerciseSetsEvent.AddSet -> {
                viewModelScope.launch {
                    try {
                        exerciseSetUseCases.addExerciseSet(
                            ExerciseSet(
                                trainingExerciseId = event.trainingExerciseId,
                                setNumber = state.value.sets.size + 1,
                                reps = event.reps,
                            )
                        )
                    } catch(e: IllegalArgumentException) {
                        _state.value = state.value.copy(
                            error = e.message
                        )
                    }
                }
            }
            is ExerciseSetsEvent.DeleteSet -> {
                viewModelScope.launch {
                    exerciseSetUseCases.deleteExerciseSet(event.exerciseSet)
                }
            }
        }
    }

    // Add this function
    fun getSetsForExercise(trainingExerciseId: String): Flow<List<ExerciseSet>> =
        exerciseSetUseCases.getSetsForTrainingExercise(trainingExerciseId)

    fun loadSets(trainingExerciseId: String) {
        getSetsJob?.cancel()
        getSetsJob = viewModelScope.launch {
            exerciseSetUseCases.getSetsForTrainingExercise(trainingExerciseId)
                .collect { sets ->
                    _state.value = state.value.copy(
                        sets = sets
                    )
                }
        }
    }
}