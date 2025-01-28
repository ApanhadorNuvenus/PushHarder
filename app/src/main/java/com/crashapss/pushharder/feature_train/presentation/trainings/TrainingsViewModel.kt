package com.crashapss.pushharder.feature_train.presentation.trainings

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crashapss.pushharder.feature_train.domain.model.Training
import com.crashapss.pushharder.feature_train.domain.use_case.exercise_set_use_cases.ExerciseSetUseCases
import com.crashapss.pushharder.feature_train.domain.use_case.exercise_use_case.ExerciseUseCases
import com.crashapss.pushharder.feature_train.domain.use_case.trainingExercise_use_case.TrainingExerciseUseCases
import com.crashapss.pushharder.feature_train.domain.use_case.training_use_case.TrainingUseCases
import com.crashapss.pushharder.feature_train.domain.util.OrderType
import com.crashapss.pushharder.feature_train.domain.util.TrainingOrder
import com.crashapss.pushharder.feature_train.presentation.trainingExercises.TrainingExerciseWithSets
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.crashapss.pushharder.feature_train.presentation.util.Event
import com.crashapss.pushharder.feature_train.presentation.util.EventBus

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

    private var cachedTrainings: List<Training> = emptyList() // Cached trainings

    private val _pendingDeletionTrainings = MutableStateFlow<List<Training>>(emptyList())
    val pendingDeletionTrainings: StateFlow<List<Training>> = _pendingDeletionTrainings.asStateFlow()

    init {
        getTrainings(TrainingOrder.Date(OrderType.Descending))
        observeExerciseUpdates()
    }

    private fun observeExerciseUpdates() {
        viewModelScope.launch {
            EventBus.events.collect { event ->
                if (event is Event.ExerciseUpdated) {
                    refreshTrainings()
                }
            }
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
                // Sort cached data instead of fetching again
                sortTrainings(event.trainingOrder)
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

    fun refreshTrainings() {
        // Just reload the data, no need to reset anything
        getTrainings(state.value.trainingOrder)
    }

    private fun sortTrainings(trainingOrder: TrainingOrder) {
        viewModelScope.launch {
            val sortedTrainings = when (trainingOrder.orderType) {
                is OrderType.Ascending -> {
                    when (trainingOrder) {
                        is TrainingOrder.Title -> cachedTrainings.sortedBy { it.title.lowercase() }
                        is TrainingOrder.Date -> cachedTrainings.sortedBy { it.date }
                        else -> cachedTrainings // Should not happen, but default case
                    }
                }
                is OrderType.Descending -> {
                    when (trainingOrder) {
                        is TrainingOrder.Title -> cachedTrainings.sortedByDescending { it.title.lowercase() }
                        is TrainingOrder.Date -> cachedTrainings.sortedByDescending { it.date }
                        else -> cachedTrainings // Should not happen, default case
                    }
                }
            }
            _state.value = state.value.copy(
                trainingOrder = trainingOrder
            )
            _trainingsState.value = sortedTrainings
        }
    }

    private fun getTrainings(trainingOrder: TrainingOrder) {
        Log.d("TrainingsViewModel", "getTrainings called")
        getTrainingsJob?.cancel()
        getTrainingsJob = trainingUseCases.getAllTrainings(trainingOrder)
            .onEach { trainings ->
                _state.value = state.value.copy(
                    trainings = trainings,
                    trainingOrder = trainingOrder
                )
                _trainingsState.value = trainings
                cachedTrainings = trainings // Cache trainings here
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
                val updatedMap = _trainingExercisesWithSets.value.toMutableMap()
                updatedMap[trainingId] = list
                _trainingExercisesWithSets.value = updatedMap
            }
        }
    }
}