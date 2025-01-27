package com.croshapss.pushharder.feature_train.presentation.trainingExercises

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.croshapss.pushharder.feature_train.domain.model.InvalidTrainingException
import com.croshapss.pushharder.feature_train.domain.model.TrainingExercise
import com.croshapss.pushharder.feature_train.domain.use_case.exercise_set_use_cases.ExerciseSetUseCases
import com.croshapss.pushharder.feature_train.domain.use_case.exercise_use_case.ExerciseUseCases
import com.croshapss.pushharder.feature_train.domain.use_case.trainingExercise_use_case.TrainingExerciseUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainingExercisesViewModel @Inject constructor(
    private val trainingExerciseUseCases: TrainingExerciseUseCases,
    private val exerciseUseCases: ExerciseUseCases,
    private val exerciseSetUseCases: ExerciseSetUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var recentlyDeletedTrainingExercise: TrainingExercise? = null

    // State
    private val _trainingExercisesWithSets = MutableStateFlow<List<TrainingExerciseWithSets>>(emptyList())
    val trainingExercisesWithSets = _trainingExercisesWithSets.asStateFlow()

    private var currentTrainingId: String? = null


    //get the id from bundle;
    //if id is nice - set local id value and load TEwSets by that id
    init {
        savedStateHandle.get<String>("trainingId")?.let { trainingId ->
            Log.d("TrainingExercisesVM", "init: trainingId from savedStateHandle: $trainingId")
            if (trainingId.isNotBlank()) {
                currentTrainingId = trainingId
                loadTrainingExercisesWithSets(trainingId)
            }
        }
        Log.e("TrainingExercises VIEWMODEL:", "\n\n\n After initialization we have TEs with SETS: \n ${trainingExercisesWithSets.value}\n\n\n")
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
                        Log.e("!!!!!!!!!!!!!!!!!!", "OBTAINED TRAINING ID IS ${event.trainingId}")
                        val trainingExercise = TrainingExercise(
                            trainingId = event.trainingId,
                            exerciseId = event.exercise.id ?: 0,
                            failure = event.failure,
                            weights = event.weights
                        )
                        trainingExerciseUseCases.addTrainingExercise(trainingExercise)
                        //reload after updating
                        loadTrainingExercisesWithSets(event.trainingId)
                    } catch (e: InvalidTrainingException) {
                        Log.e("TrainingExercisesVM", "Error adding training exercise: ${e.message}")
                    }
                }
            }
        }
    }

    private fun loadTrainingExercisesWithSets(trainingId: String) {
        viewModelScope.launch {
            trainingExerciseUseCases.getExercisesForTraining(trainingId).collect { trainingExercises ->
                val list = trainingExercises.map { trainingExercise ->
                    val exercise = exerciseUseCases.getExerciseById(trainingExercise.exerciseId).firstOrNull()
                    val sets = exerciseSetUseCases.getSetsForTrainingExercise(trainingExercise.id).firstOrNull().orEmpty()
                    TrainingExerciseWithSets(
                        trainingExercise = trainingExercise,
                        exercise = exercise,
                        sets = sets
                    )
                }
                _trainingExercisesWithSets.value = list
            }
        }
    }
}