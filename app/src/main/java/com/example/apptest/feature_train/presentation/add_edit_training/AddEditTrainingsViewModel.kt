package com.example.apptest.feature_train.presentation.add_edit_training

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apptest.feature_train.domain.model.InvalidTrainingException
import com.example.apptest.feature_train.domain.model.Training
import com.example.apptest.feature_train.domain.use_case.training_use_case.TrainingUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.apptest.feature_train.domain.model.Exercise
import com.example.apptest.feature_train.domain.use_case.exercise_use_case.ExerciseUseCases
import com.example.apptest.feature_train.domain.use_case.trainingExercise_use_case.TrainingExerciseUseCases
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.Flow
import com.example.apptest.feature_train.domain.model.ExerciseType
import com.example.apptest.feature_train.domain.model.TrainingExercise
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import java.util.UUID
import kotlin.random.Random

@HiltViewModel
class AddEditTrainingsViewModel @Inject constructor(
    private val trainingUseCases: TrainingUseCases,
    private val trainingExercisesUseCases: TrainingExerciseUseCases,
    val exerciseUseCases: ExerciseUseCases,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _trainingState = mutableStateOf(AddEditCurrentTrainingState())
    val trainingState: State<AddEditCurrentTrainingState> = _trainingState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _currentTrainingId = MutableStateFlow<String?>(null)
    val currentTrainingId = _currentTrainingId.asStateFlow()

    init {
        val trainingId = savedStateHandle.get<String>("trainingId") ?: ""
        
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
                        trainingId = training.id, // Important!
                        isTitleHintVisible = false
                    )
                    
                    // Load associated exercises
                    val exercises = trainingExercisesUseCases.getExercisesForTraining(training.id).first()
                    _trainingState.value = _trainingState.value.copy(
                        temporaryExercises = exercises.toMutableList()
                    )
                }
            }
        } else {
            // Create new training with temporary ID
            createNewTraining()
        }
    }

    private fun createNewTraining() {
        viewModelScope.launch {
            try {
                val currentTime = System.currentTimeMillis()
                val newTraining = Training(
                    id = UUID.randomUUID().toString(),
                    title = "",
                    date = currentTime, // This is correct
                    failure = false,
                    weights = null
                )
                trainingUseCases.addTraining(newTraining)
                _currentTrainingId.value = newTraining.id
                _trainingState.value = _trainingState.value.copy(
                    trainingId = newTraining.id,
                    title = newTraining.title,
                    date = currentTime, // Make sure state has correct date
                    isTitleHintVisible = true,
                    failure = newTraining.failure,
                    weights = null,
                    isWeightHintVisible = true,
                    temporaryExercises = mutableListOf()
                )
            } catch (e: Exception) {
                Log.e("AddEditTrainingsVM", "Error creating new training: ${e.message}")
                _eventFlow.emit(UiEvent.ShowSnackbar("Error creating new training"))
            }
        }
    }

    fun onEvent(event: AddEditTrainingsEvent) {
        when (event) {
            is AddEditTrainingsEvent.EnteredTitle -> {
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
                _trainingState.value = trainingState.value.copy(
                    failure = event.value
                )
            }

            is AddEditTrainingsEvent.AddExercise -> {
                val newExercise = TrainingExercise(
                    trainingId = _currentTrainingId.value ?: "", // Use temporary training ID
                    exerciseId = event.exercise.id!!,
                    reps = event.reps,
                    duration = event.duration
                )

                _trainingState.value.temporaryExercises.add(newExercise)
                _trainingState.value = trainingState.value.copy(
                    temporaryExercises = _trainingState.value.temporaryExercises
                )
            }

            is AddEditTrainingsEvent.DeleteTemporaryExercise -> {
                // Create a new mutable list from the current list
                val updatedExercises = _trainingState.value.temporaryExercises.toMutableList()
                // Remove the exercise
                updatedExercises.remove(event.trainingExercise)
                // Update state with new list
                _trainingState.value = trainingState.value.copy(
                    temporaryExercises = updatedExercises
                )
            }

            is AddEditTrainingsEvent.SaveTraining -> {
                viewModelScope.launch {
                    try {
                        // Validation checks
                        if (trainingState.value.title.isBlank()) {
                            throw InvalidTrainingException("The title can't be empty.")
                        }

                        val weightsAsLong = trainingState.value.weights?.toLongOrNull()

                        // Create training object
                        val training = Training(
                            id = _currentTrainingId.value ?: UUID.randomUUID().toString(),
                            title = trainingState.value.title,
                            date = trainingState.value.date,
                            failure = trainingState.value.failure,
                            weights = weightsAsLong
                        )

                        if (_currentTrainingId.value == null) {
                            // Save new training
                            _currentTrainingId.value = training.id
                            trainingUseCases.addTraining(training)

                            // Save all exercises with the new training ID
                            _trainingState.value.temporaryExercises.forEach { exercise ->
                                trainingExercisesUseCases.addTrainingExercise(
                                    exercise.copy(trainingId = training.id)
                                )
                            }
                        } else {
                            // Update existing training
                            trainingUseCases.updateTraining(training)

                            // Get current exercises from database
                            val currentExercises =
                                trainingExercisesUseCases.getExercisesForTraining(_currentTrainingId.value!!)
                                    .first()

                            // Find exercises to delete
                            val exercisesToDelete = currentExercises.filter { dbExercise ->
                                !_trainingState.value.temporaryExercises.any { it.id == dbExercise.id }
                            }

                            // Delete removed exercises
                            exercisesToDelete.forEach { exercise ->
                                trainingExercisesUseCases.deleteTrainingExercise(exercise)
                            }

                            // Update or add exercises
                            _trainingState.value.temporaryExercises.forEach { exercise ->
                                trainingExercisesUseCases.addTrainingExercise(
                                    exercise.copy(trainingId = _currentTrainingId.value!!)
                                )
                            }
                        }

                        _eventFlow.emit(UiEvent.SaveTraining)
                    } catch (e: InvalidTrainingException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save training"
                            )
                        )
                    }
                }
            }
        }
    }
    fun getExercise(exerciseId: Int): Flow<Exercise?> {
        return exerciseUseCases.getExerciseById(exerciseId)
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveTraining : UiEvent()
    }
}