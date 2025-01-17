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
import com.example.apptest.feature_train.domain.use_case.exercise_set_use_cases.ExerciseSetUseCases
import com.example.apptest.feature_train.domain.use_case.exercise_use_case.ExerciseUseCases
import com.example.apptest.feature_train.domain.use_case.trainingExercise_use_case.TrainingExerciseUseCases
import com.example.apptest.feature_train.domain.use_case.training_use_case.TrainingUseCases
import com.example.apptest.feature_train.presentation.trainingExercises.TrainingExerciseWithSets
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddEditTrainingsViewModel @Inject constructor(
    private val trainingUseCases: TrainingUseCases,
    private val trainingExercisesUseCases: TrainingExerciseUseCases,
    val exerciseUseCases: ExerciseUseCases,
    private val exerciseSetUseCases: ExerciseSetUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _trainingState = mutableStateOf(AddEditCurrentTrainingState())
    val trainingState: State<AddEditCurrentTrainingState> = _trainingState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _trainingExercisesWithSets = MutableStateFlow<List<TrainingExerciseWithSets>>(emptyList())
    val trainingExercisesWithSets: StateFlow<List<TrainingExerciseWithSets>> = _trainingExercisesWithSets.asStateFlow()

    private var currentTraining: Training? = null // Temporary Training (in-memory)
    private var previousTrainingExercisesWithSets: List<TrainingExerciseWithSets> = emptyList()

    init {
        savedStateHandle.get<String>("trainingId")?.let { trainingId ->
            if (trainingId.isNotBlank()) {
                // Load existing training
                viewModelScope.launch {
                    trainingUseCases.getTrainingById(trainingId)?.let { training ->
                        currentTraining = training
                        _trainingState.value = trainingState.value.copy(
                            trainingId = training.id,
                            title = training.title,
                            date = training.date,
                            failure = training.failure,
                            weights = training.weights?.toString(),
                            isTitleHintVisible = false
                        )
                        loadTrainingExercisesWithSets(trainingId)
                    }
                }
            } else {
                // Initialize for a new training (no immediate saving)
                currentTraining = Training(
                    id = UUID.randomUUID().toString(),
                    title = "",
                    date = System.currentTimeMillis(),
                    failure = false,
                    weights = null
                )
                _trainingState.value = trainingState.value.copy(
                    trainingId = currentTraining!!.id, // Set the new ID
                    date = currentTraining!!.date,
                    isTitleHintVisible = true,
                    isWeightHintVisible = true
                )
            }
        }
    }

    fun onEvent(event: AddEditTrainingsEvent) {
        when (event) {
            is AddEditTrainingsEvent.EnteredTitle -> {
                // Update temporary training
                currentTraining = currentTraining?.copy(title = event.value)
                _trainingState.value = trainingState.value.copy(
                    title = event.value,
                    isTitleHintVisible = event.value.isBlank()
                )
            }
            is AddEditTrainingsEvent.ChangeTitleFocus -> {
                _trainingState.value = trainingState.value.copy(
                    isTitleHintVisible = !event.focusState.isFocused && trainingState.value.title.isBlank()
                )
            }
            is AddEditTrainingsEvent.EnteredWeight -> {
                // Update temporary training
                currentTraining = currentTraining?.copy(weights = event.value.toFloatOrNull())
                _trainingState.value = trainingState.value.copy(
                    weights = event.value,
                    isWeightHintVisible = event.value.isBlank()
                )
            }
            is AddEditTrainingsEvent.ChangeWeightFocus -> {
                _trainingState.value = trainingState.value.copy(
                    isWeightHintVisible = !event.focusState.isFocused && trainingState.value.weights.isNullOrBlank()
                )
            }
            is AddEditTrainingsEvent.ChangeFailureState -> {
                // Update temporary training
                currentTraining = currentTraining?.copy(failure = event.value)
                _trainingState.value = trainingState.value.copy(
                    failure = event.value
                )
            }
            is AddEditTrainingsEvent.AddExercise -> {
                val newTrainingExerciseId = UUID.randomUUID().toString()
                val newTrainingExercise = TrainingExercise(
                    trainingId = currentTraining?.id ?: "",
                    exerciseId = event.exercise.id!!,
                    id = newTrainingExerciseId
                )
                val currentExercises = _trainingExercisesWithSets.value.toMutableList()
                currentExercises.add(
                    TrainingExerciseWithSets(
                        trainingExercise = newTrainingExercise,
                        exercise = event.exercise,
                        sets = mutableListOf()
                    )
                )
                _trainingExercisesWithSets.value = currentExercises
            }
            is AddEditTrainingsEvent.AddSetToExercise -> {
                val newSet = ExerciseSet(
                    id = UUID.randomUUID().toString(),
                    trainingExerciseId = event.trainingExerciseId,
                    setNumber = event.setNumber,
                    reps = event.reps
                )
                val updatedExercises = _trainingExercisesWithSets.value.map {
                    if (it.trainingExercise.id == event.trainingExerciseId) {
                        it.copy(sets = (it.sets + newSet).toMutableList())
                    } else {
                        it
                    }
                }
                _trainingExercisesWithSets.value = updatedExercises
            }
            is AddEditTrainingsEvent.DeleteSetFromExercise -> {
                val updatedExercises = _trainingExercisesWithSets.value.map {
                    if (it.trainingExercise.id == event.trainingExerciseId) {
                        it.copy(sets = it.sets.filter { set -> set.id != event.setId }.toMutableList())
                    } else {
                        it
                    }
                }.toMutableList()
                _trainingExercisesWithSets.value = updatedExercises
            }
            is AddEditTrainingsEvent.DeleteTrainingExercise -> {
                val updatedExercises = _trainingExercisesWithSets.value.filter {
                    it.trainingExercise.id != event.trainingExerciseId
                }
                _trainingExercisesWithSets.value = updatedExercises
            }
            is AddEditTrainingsEvent.SaveTraining -> {
                // Now we save to DB
                viewModelScope.launch {
                    try {
                        currentTraining?.let { training ->
                            trainingUseCases.addTraining(training) // Insert or update
                            saveTrainingExerciseWithSets(training.id) // Save associated exercises and sets
                        }
                        _eventFlow.emit(UiEvent.SaveTraining)
                    } catch (e: Exception) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = "Couldn't save training: ${e.message}"
                            )
                        )
                    }
                }
            }
        }
    }

    private fun saveTrainingExerciseWithSets(trainingId: String) {
        val currentTrainingExercisesWithSets = _trainingExercisesWithSets.value
        val previousTrainingExercisesWithSets = this.previousTrainingExercisesWithSets
        viewModelScope.launch {
            // Determine which training exercises to delete
            val exercisesToDelete = previousTrainingExercisesWithSets.filter { oldExercise ->
                currentTrainingExercisesWithSets.none { it.trainingExercise.id == oldExercise.trainingExercise.id }
            }
            // Delete the training exercises from DB
            exercisesToDelete.forEach {
                trainingExercisesUseCases.deleteTrainingExercise(it.trainingExercise)
            }

            currentTrainingExercisesWithSets.forEach { item ->
                val trainingExercise = item.trainingExercise.copy(trainingId = trainingId)
                trainingExercisesUseCases.addTrainingExercise(trainingExercise)

                // Check which sets to delete for that TrainingExercise
                val oldSets = previousTrainingExercisesWithSets.find { it.trainingExercise.id == item.trainingExercise.id }?.sets ?: emptyList()
                val currentSets = item.sets
                val setsToDelete = oldSets.filter { oldSet ->
                    currentSets.none { it.id == oldSet.id }
                }
                // Delete the old sets that should not exist
                setsToDelete.forEach{ exerciseSetUseCases.deleteExerciseSet(it)}

                item.sets.forEach { set ->
                    exerciseSetUseCases.addExerciseSet(set.copy(trainingExerciseId = trainingExercise.id))
                }
            }
            // Update previous state to new one after changes are saved
            this@AddEditTrainingsViewModel.previousTrainingExercisesWithSets = currentTrainingExercisesWithSets
        }

    }

    private fun loadTrainingExercisesWithSets(trainingId: String) {
        viewModelScope.launch {
            trainingExercisesUseCases.getExercisesForTraining(trainingId).collect { trainingExercises ->
                val list = trainingExercises.map { trainingExercise ->
                    val exercise = exerciseUseCases.getExerciseById(trainingExercise.exerciseId).firstOrNull()
                    val sets = exerciseSetUseCases.getSetsForTrainingExercise(trainingExercise.id).firstOrNull().orEmpty()
                    TrainingExerciseWithSets(
                        trainingExercise = trainingExercise,
                        exercise = exercise,
                        sets = sets
                    )
                }
                _trainingExercisesWithSets.update { list }
                this@AddEditTrainingsViewModel.previousTrainingExercisesWithSets = list
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveTraining : UiEvent()
    }
}