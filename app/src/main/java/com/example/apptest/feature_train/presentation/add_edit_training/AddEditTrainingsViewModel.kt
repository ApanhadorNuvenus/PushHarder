package com.example.apptest.feature_train.presentation.add_edit_training

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptest.feature_train.domain.model.Exercise
import com.example.apptest.feature_train.domain.model.ExerciseSet
import com.example.apptest.feature_train.domain.model.InvalidTrainingException
import com.example.apptest.feature_train.domain.model.Training
import com.example.apptest.feature_train.domain.model.TrainingExercise
import com.example.apptest.feature_train.domain.use_case.exercise_use_case.ExerciseUseCases
import com.example.apptest.feature_train.domain.use_case.exercise_set_use_cases.ExerciseSetUseCases
import com.example.apptest.feature_train.domain.use_case.trainingExercise_use_case.TrainingExerciseUseCases
import com.example.apptest.feature_train.domain.use_case.training_use_case.TrainingUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddEditTrainingsViewModel @Inject constructor(
    private val trainingUseCases: TrainingUseCases,
    private val trainingExercisesUseCases: TrainingExerciseUseCases,
    val exerciseUseCases: ExerciseUseCases,
    val exerciseSetUseCases: ExerciseSetUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _trainingState = mutableStateOf(AddEditCurrentTrainingState())
    val trainingState: State<AddEditCurrentTrainingState> = _trainingState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _currentTrainingId = MutableStateFlow<String?>(null)
    val currentTrainingId = _currentTrainingId.asStateFlow()


    private val _trainingExerciseSets = MutableStateFlow<Map<String,List<ExerciseSet>>>(emptyMap())
    val trainingExerciseSets = _trainingExerciseSets.asStateFlow()

    init {
        savedStateHandle.get<String>("trainingId")?.let { trainingId ->
            if (trainingId.isNotBlank()) {
                // Load existing training
                viewModelScope.launch {
                    trainingUseCases.getTrainingById(trainingId)?.let { training ->
                        _currentTrainingId.value = training.id
                        _trainingState.value = trainingState.value.copy(
                            title = training.title,
                            date = training.date,
                            failure = training.failure,
                            weights = training.weights?.toString(),
                            trainingId = training.id,
                            isTitleHintVisible = false
                        )
                        loadTrainingExercises(trainingId)
                    }
                }
            } else {
                // Create new training with temporary ID
                createNewTraining()
            }
        } ?: run {
            // Handle the case where trainingId is null (shouldn't happen with the current navigation setup)
            createNewTraining()
        }
    }

    private fun createNewTraining() {
        viewModelScope.launch {
            val currentTime = System.currentTimeMillis()
            val newTrainingId = UUID.randomUUID().toString()
            _currentTrainingId.value = newTrainingId
            _trainingState.value = _trainingState.value.copy(
                trainingId = newTrainingId,
                title = "",
                date = currentTime,
                isTitleHintVisible = true,
                failure = false,
                weights = null,
                isWeightHintVisible = true,
                trainingExercises = mutableListOf()
            )
            try {
                trainingUseCases.addTraining(
                    Training(
                        id = newTrainingId,
                        title = "",
                        date = currentTime,
                        failure = false,
                        weights = null
                    )
                )
            } catch (e: Exception) {
                Log.e("AddEditTrainingsVM", "Error creating new training: ${e.message}")
                _eventFlow.emit(UiEvent.ShowSnackbar("Error creating new training"))
            }
        }
    }

    private fun loadTrainingExercises(trainingId: String) {
        viewModelScope.launch {
            trainingExercisesUseCases.getExercisesForTraining(trainingId)
                .collect { trainingExercises ->
                    _trainingState.value = _trainingState.value.copy(
                        trainingExercises = trainingExercises.toMutableList()
                    )
                    loadAllExerciseSets(trainingExercises)
                }
        }
    }

    private  fun loadAllExerciseSets(trainingExercises: List<TrainingExercise>) {
        viewModelScope.launch {
            val setsMap = trainingExercises.associate {te ->
                te.id to exerciseSetUseCases.getSetsForTrainingExercise(te.id).firstOrNull().orEmpty()
            }
            _trainingExerciseSets.value = setsMap
        }
    }

    fun onEvent(event: AddEditTrainingsEvent) {
        when (event) {
            is AddEditTrainingsEvent.EnteredTitle -> {
                _trainingState.value = trainingState.value.copy(
                    title = event.value,
                    isTitleHintVisible = event.value.isBlank()
                )
                saveTraining()
            }

            is AddEditTrainingsEvent.ChangeTitleFocus -> {
                _trainingState.value = trainingState.value.copy(
                    isTitleHintVisible = !event.focusState.isFocused && trainingState.value.title.isBlank()
                )
            }

            is AddEditTrainingsEvent.EnteredWeight -> {
                _trainingState.value = trainingState.value.copy(
                    weights = event.value,
                    isWeightHintVisible = event.value.isBlank()
                )
                saveTraining()
            }

            is AddEditTrainingsEvent.ChangeWeightFocus -> {
                _trainingState.value = trainingState.value.copy(
                    isWeightHintVisible = !event.focusState.isFocused && trainingState.value.weights.isNullOrBlank()
                )
            }

            is AddEditTrainingsEvent.ChangeFailureState -> {
                _trainingState.value = trainingState.value.copy(
                    failure = event.value
                )
                saveTraining()
            }

            is AddEditTrainingsEvent.AddExercise -> {
                val trainingId = _currentTrainingId.value
                if (trainingId != null) {
                    viewModelScope.launch {
                        // Create the TrainingExercise immediately
                        val newTrainingExercise = TrainingExercise(
                            trainingId = trainingId,
                            exerciseId = event.exercise.id!!
                        )
                        trainingExercisesUseCases.addTrainingExercise(newTrainingExercise)

                        // Emit an event to signal the UI to prompt for the first set
                        _eventFlow.emit(UiEvent.PromptForSet(newTrainingExercise))
                        loadTrainingExercises(trainingId)
                    }
                }
            }

            is AddEditTrainingsEvent.AddSet -> {
                viewModelScope.launch {
                    val newSet = ExerciseSet(
                        trainingExerciseId = event.trainingExercise.id,
                        setNumber = event.setNumber,
                        reps = event.reps
                    )
                    exerciseSetUseCases.addExerciseSet(newSet)
                    val setsMap = _trainingExerciseSets.value.toMutableMap()
                    val existingSets = setsMap[event.trainingExercise.id].orEmpty().toMutableList()
                    existingSets.add(newSet)
                    setsMap[event.trainingExercise.id] = existingSets
                    _trainingExerciseSets.value = setsMap
                }
            }

            is AddEditTrainingsEvent.DeleteSet -> {
                viewModelScope.launch {
                    exerciseSetUseCases.deleteExerciseSet(event.exerciseSet)
                    val setsMap = _trainingExerciseSets.value.toMutableMap()
                    val existingSets = setsMap[event.exerciseSet.trainingExerciseId].orEmpty().toMutableList()
                    existingSets.remove(event.exerciseSet)
                    setsMap[event.exerciseSet.trainingExerciseId] = existingSets
                    _trainingExerciseSets.value = setsMap
                }
            }

            is AddEditTrainingsEvent.DeleteTrainingExercise -> {
                viewModelScope.launch {
                    trainingExercisesUseCases.deleteTrainingExercise(event.trainingExercise)
                    // Refresh the list of TrainingExercises
                    _currentTrainingId.value?.let { loadTrainingExercises(it) }
                }
            }
        }
    }
    private fun saveTraining() {
        viewModelScope.launch {
            try {
                val training = Training(
                    id = _currentTrainingId.value ?: "",
                    title = _trainingState.value.title,
                    date = _trainingState.value.date,
                    failure = _trainingState.value.failure,
                    weights = _trainingState.value.weights?.toFloatOrNull()
                )
                trainingUseCases.addTraining(training) // Use addTraining for both create and update
            } catch (e: InvalidTrainingException) {
                _eventFlow.emit(UiEvent.ShowSnackbar(e.message ?: "Couldn't save training"))
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data class PromptForSet(val trainingExercise: TrainingExercise) : UiEvent()
    }
}