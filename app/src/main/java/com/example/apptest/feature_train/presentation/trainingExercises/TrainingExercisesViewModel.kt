package com.example.apptest.feature_train.presentation.trainingExercises

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptest.feature_train.domain.model.Exercise
import com.example.apptest.feature_train.domain.model.InvalidTrainingException
import com.example.apptest.feature_train.domain.model.TrainingExercise
import com.example.apptest.feature_train.domain.use_case.exercise_use_case.ExerciseUseCases
import com.example.apptest.feature_train.domain.use_case.trainingExercise_use_case.TrainingExerciseUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingExercisesViewModel @Inject constructor(
    private val trainingExerciseUseCases: TrainingExerciseUseCases,
    val exerciseUseCases: ExerciseUseCases,
    savedStateHandle: SavedStateHandle

) : ViewModel() {
    private var recentlyDeletedTrainingExercise: TrainingExercise? = null
    private var getTrainingExercisesJob: Job? = null

    // state of this screen
    private val _state = mutableStateOf(TrainingExercisesState())
    val state: State<TrainingExercisesState> = _state
    private var currentTrainingId: Int? = null

    init {
        savedStateHandle.get<Int>("trainingId")?.let { trainingId ->
            Log.d("TrainingExercisesVM", "init: trainingId from savedStateHandle: $trainingId")
            if (trainingId != -1) {
                currentTrainingId = trainingId
            }
        }
    }

    fun onEvent(event: TrainingExercisesEvent) {
        when (event) {
            is TrainingExercisesEvent.DeleteTrainingExercise -> {
                viewModelScope.launch {
                    trainingExerciseUseCases.deleteTrainingExercise(event.trainingExercise)
                    recentlyDeletedTrainingExercise = event.trainingExercise
                }
            }
            is TrainingExercisesEvent.RestoreExerciseTraining -> {
                viewModelScope.launch {
                    trainingExerciseUseCases.addTrainingExercise(
                        recentlyDeletedTrainingExercise ?: return@launch
                    )
                    recentlyDeletedTrainingExercise = null
                }
            }
            is TrainingExercisesEvent.AddTrainingExercise -> {
                viewModelScope.launch {
                    try {
                        // Add the exercise to the training
                        // sets are fetched on their own way, here we don't operate on them
                        val trainingExercise = TrainingExercise(
                            trainingId = event.trainingId,
                            exerciseId = event.exercise.id ?: 0
                        )

                        trainingExerciseUseCases.addTrainingExercise(
                            trainingExercise
                        )

                        getTrainingExercises(event.trainingId)

                    } catch (e: InvalidTrainingException) {
                        // Handle validation errors, e.g., show a snackbar
                    }
                }
            }
        }
    }

    //  fetch TEs when needed, state is changed only here
    fun getTrainingExercises(trainingId: String): Flow<List<TrainingExercise>> {
        return trainingExerciseUseCases.getExercisesForTraining(trainingId)
    }

    fun getExercise(exerciseId: Int): Flow<Exercise?> {
        return exerciseUseCases.getExerciseById(exerciseId)
            .flowOn(Dispatchers.IO)
    }
}