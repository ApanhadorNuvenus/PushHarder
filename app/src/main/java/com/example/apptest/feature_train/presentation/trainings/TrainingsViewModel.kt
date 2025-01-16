package com.example.apptest.feature_train.presentation.trainings

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptest.feature_train.domain.model.Exercise
import com.example.apptest.feature_train.domain.model.ExerciseSet
import com.example.apptest.feature_train.domain.model.Training
import com.example.apptest.feature_train.domain.model.TrainingExercise
import com.example.apptest.feature_train.domain.use_case.training_use_case.TrainingUseCases
import com.example.apptest.feature_train.domain.util.OrderType
import com.example.apptest.feature_train.domain.util.TrainingOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.apptest.feature_train.domain.use_case.exercise_set_use_cases.ExerciseSetUseCases
import com.example.apptest.feature_train.domain.use_case.exercise_use_case.ExerciseUseCases
import com.example.apptest.feature_train.domain.use_case.trainingExercise_use_case.TrainingExerciseUseCases
import com.example.apptest.feature_train.presentation.trainingExercises.TrainingExerciseWithSets
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull

@HiltViewModel
class TrainingsViewModel @Inject constructor(
    private val trainingUseCases: TrainingUseCases,
    private val trainingExerciseUseCases: TrainingExerciseUseCases,
    val exerciseUseCases: ExerciseUseCases,
    val exerciseSetUseCases: ExerciseSetUseCases
) : ViewModel() {

    private var trainingToDelete: Training? = null
    private var deleteTrainingJob: Job? = null
    private var getTrainingsJob: Job? = null

    private val _state = mutableStateOf(TrainingsState())
    val state: State<TrainingsState> = _state

    private val _trainingExercisesWithSets = MutableStateFlow<Map<String, List<TrainingExerciseWithSets>>>(emptyMap())
    val trainingExercisesWithSets: StateFlow<Map<String, List<TrainingExerciseWithSets>>> = _trainingExercisesWithSets.asStateFlow()

    init {
        getTrainings(TrainingOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: TrainingsEvent) {
        when (event) {
            is TrainingsEvent.Order -> {
                if (state.value.trainingOrder::class == event.trainingOrder::class &&
                    state.value.trainingOrder.orderType == event.trainingOrder.orderType
                ) {
                    return
                }
                getTrainings(event.trainingOrder)
            }

            is TrainingsEvent.DeleteTraining -> {
                trainingToDelete = event.training
                // Update UI immediately
                _state.value = state.value.copy(
                    trainings = state.value.trainings.filter { it.id != event.training.id }
                )
                // Schedule actual deletion
                viewModelScope.launch {
                    deleteTrainingJob?.cancel()
                    deleteTrainingJob = launch {
                        kotlinx.coroutines.delay(4000L)
                        trainingToDelete?.let { training ->
                            trainingUseCases.deleteTraining(training)
                            trainingToDelete = null
                        }
                    }
                }
            }

            is TrainingsEvent.RestoreTraining -> {
                deleteTrainingJob?.cancel()
                trainingToDelete?.let { training ->
                    // Restore UI immediately
                    _state.value = state.value.copy(
                        trainings = state.value.trainings + training
                    )
                    trainingToDelete = null
                }
            }

            is TrainingsEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getTrainings(trainingOrder: TrainingOrder) {
        getTrainingsJob?.cancel()
        getTrainingsJob = trainingUseCases.getAllTrainings(trainingOrder)
            .onEach { trainings ->
                _state.value = state.value.copy(
                    trainings = trainings,
                    trainingOrder = trainingOrder
                )
                // Load exercises for each training
                trainings.forEach { training ->
                    loadTrainingExercisesWithSets(training.id)
                }
            }
            .launchIn(viewModelScope)
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
                // Update the map with the new list of exercises for the specific training
                val updatedMap = _trainingExercisesWithSets.value.toMutableMap()
                updatedMap[trainingId] = list
                _trainingExercisesWithSets.value = updatedMap
            }
        }
    }

    fun getTrainingExercisesWithSets(trainingId: String): List<TrainingExerciseWithSets> {
        return _trainingExercisesWithSets.value[trainingId] ?: emptyList()
    }

}