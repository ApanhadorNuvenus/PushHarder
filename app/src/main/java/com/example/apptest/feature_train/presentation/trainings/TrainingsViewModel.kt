package com.example.apptest.feature_train.presentation.trainings

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptest.feature_train.domain.model.Training
import com.example.apptest.feature_train.domain.use_case.exercise_set_use_cases.ExerciseSetUseCases
import com.example.apptest.feature_train.domain.use_case.exercise_use_case.ExerciseUseCases
import com.example.apptest.feature_train.domain.use_case.trainingExercise_use_case.TrainingExerciseUseCases
import com.example.apptest.feature_train.domain.use_case.training_use_case.TrainingUseCases
import com.example.apptest.feature_train.domain.util.OrderType
import com.example.apptest.feature_train.domain.util.TrainingOrder
import com.example.apptest.feature_train.presentation.trainingExercises.TrainingExerciseWithSets
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _trainingsState = MutableStateFlow<List<Training>>(emptyList())
    val trainingsState: StateFlow<List<Training>> = _trainingsState.asStateFlow()

    private var dataLoaded = false

    private val _pendingDeletionTrainings = MutableStateFlow<List<Training>>(emptyList())
    val pendingDeletionTrainings: StateFlow<List<Training>> = _pendingDeletionTrainings.asStateFlow()

    init {
        Log.d("TrainingsViewModel", "ViewModel init block called")
        if (!dataLoaded) {
            getTrainings(TrainingOrder.Date(OrderType.Descending))
            dataLoaded = true
        }
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
                viewModelScope.launch {
                    // Immediately update the UI
                    _trainingsState.value = _trainingsState.value.filter { it.id != event.training.id }
                    _pendingDeletionTrainings.value = _pendingDeletionTrainings.value + event.training
                    // Do not delete from DB here, just store it for potential deletion later
                }
            }
            is TrainingsEvent.RestoreTraining -> {
                deleteTrainingJob?.cancel()
                _pendingDeletionTrainings.value.find { it.id == trainingToDelete?.id }?.let { training ->
                    // Restore UI immediately
                    _trainingsState.value = _trainingsState.value + training
                    _pendingDeletionTrainings.value = _pendingDeletionTrainings.value - training
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

    fun confirmDeletion(training: Training) {
        viewModelScope.launch {
            trainingUseCases.deleteTraining(training)
            _pendingDeletionTrainings.value = _pendingDeletionTrainings.value - training
        }
    }

    fun handlePendingDeletions() {
        viewModelScope.launch {
            _pendingDeletionTrainings.value.forEach { training ->
                trainingUseCases.deleteTraining(training)
            }
            _pendingDeletionTrainings.value = emptyList()
        }
    }
    private fun getTrainings(trainingOrder: TrainingOrder) {
        Log.d("TrainingsViewModel", "getTrainings called")
        getTrainingsJob?.cancel()
        getTrainingsJob = trainingUseCases.getAllTrainings(trainingOrder)
            .onStart {
                _isLoading.value = true
                delay(500)
            }
            .onEach { trainings ->
                _state.value = state.value.copy(
                    trainings = trainings,
                    trainingOrder = trainingOrder
                )
                _trainingsState.value = trainings
                trainings.forEach { training ->
                    loadTrainingExercisesWithSets(training.id)
                }
            }
            .onCompletion {
                viewModelScope.launch {
                    delay(550L)
                    _isLoading.value = false
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadTrainingExercisesWithSets(trainingId: String) {
        viewModelScope.launch {
            trainingExerciseUseCases.getExercisesForTraining(trainingId).onStart{
                _isLoading.value = true
            }.collect { trainingExercises ->
                val list = trainingExercises.map { trainingExercise ->
                    val exercise = exerciseUseCases.getExerciseById(trainingExercise.exerciseId).firstOrNull()
                    val sets = exerciseSetUseCases.getSetsForTrainingExercise(trainingExercise.id).firstOrNull().orEmpty()
                    TrainingExerciseWithSets(
                        trainingExercise = trainingExercise,
                        exercise = exercise,
                        sets = sets
                    )
                }
                val updatedMap = _trainingExercisesWithSets.value.toMutableMap()
                updatedMap[trainingId] = list
                _trainingExercisesWithSets.value = updatedMap
                _isLoading.value = false
            }
        }
    }
}