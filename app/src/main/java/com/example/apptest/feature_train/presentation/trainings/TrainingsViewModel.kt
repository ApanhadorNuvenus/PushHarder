package com.example.apptest.feature_train.presentation.trainings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptest.feature_train.domain.model.Training
import com.example.apptest.feature_train.domain.use_case.training_use_case.TrainingUseCases
import com.example.apptest.feature_train.domain.util.OrderType
import com.example.apptest.feature_train.domain.util.TrainingOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.material3.SnackbarDuration
import kotlinx.coroutines.delay

@HiltViewModel
class TrainingsViewModel @Inject constructor(
    private val trainingUseCases: TrainingUseCases
) : ViewModel() {


    private var trainingToDelete: Training? = null
    private var deleteTrainingJob: Job? = null
    private var getTrainingsJob: Job? = null

    private val _state = mutableStateOf(TrainingsState())
    val state: State<TrainingsState> = _state

    init {
        getTrainings(TrainingOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: TrainingsEvent) {
        when(event) {
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
                        delay(4000L)
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
            }
            .launchIn(viewModelScope)
    }
}