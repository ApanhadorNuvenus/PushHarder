package com.example.apptest.feature_train.presentation.sets

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptest.feature_train.domain.model.Exercise
import com.example.apptest.feature_train.domain.model.ExerciseSet
import com.example.apptest.feature_train.domain.model.ExerciseType
import com.example.apptest.feature_train.domain.model.InvalidTrainingException
import com.example.apptest.feature_train.domain.model.TrainingExercise
import com.example.apptest.feature_train.domain.use_case.exercise_use_case.ExerciseUseCases
import com.example.apptest.feature_train.domain.use_case.set.ExerciseSetUseCases
import com.example.apptest.feature_train.domain.use_case.trainingExercise_use_case.TrainingExerciseUseCases
import com.example.apptest.feature_train.presentation.trainingExercises.TrainingExercisesEvent
import com.example.apptest.feature_train.presentation.trainingExercises.TrainingExercisesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseSetsViewModel @Inject constructor(
    private val exerciseSetUseCases: ExerciseSetUseCases,
    private val trainingExerciseUseCases: TrainingExerciseUseCases,
    val exerciseUseCases: ExerciseUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    //states
    private val _state = mutableStateOf(ExerciseSetsState())
    val state: State<ExerciseSetsState> = _state
    private var currentTEId: String? = null

    init {
        // TODO: HANDLE TRAINING EXERCISE ID???
    }

    fun onEvent(event: ExerciseSetsEvent) {
        when (event) {
            is ExerciseSetsEvent.deleteExerciseSet -> {
                viewModelScope.launch {
                    exerciseSetUseCases.deleteExerciseSet(event.set)
                }
            }
            is ExerciseSetsEvent.addExerciseSet -> {
                viewModelScope.launch {
                    try {
//                        // Validate reps and duration based on exercise type
//                        when (event.exercise.exerciseType) {
//                            is ExerciseType.Reps -> {
//                                if (event.reps == null || event.reps <= 0) {
//                                    throw InvalidTrainingException("Reps must be greater than zero for Reps type exercises.")
//                                }
//                            }
//                            is ExerciseType.Duration -> {
//                                if (event.duration == null || event.duration <= 0) {
//                                    throw InvalidTrainingException("Duration must be greater than zero for Duration type exercises.")
//                                }
//                            }
//                        }

                        val exerciseSet = ExerciseSet(
                            teId = event.teID,
                            sets = event.sets,
                            reps = event.reps
                        )

                        exerciseSetUseCases.addExerciseSet(exerciseSet)

                        getExerciseSets(teID = event.teID)

                    } catch (e: Exception) {
                        // Handle validation errors, e.g., show a snackbar
                    }
                }
            }
        }
    }

    fun getExerciseSets(teID: String): Flow<List<ExerciseSet>> {
        return exerciseSetUseCases.getExerciseSetsForTE(teID)
    }
}