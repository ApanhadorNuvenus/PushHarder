package com.croshapss.pushharder.feature_train.presentation.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.croshapss.pushharder.feature_train.domain.model.Training
import com.croshapss.pushharder.feature_train.domain.use_case.exercise_set_use_cases.ExerciseSetUseCases
import com.croshapss.pushharder.feature_train.domain.use_case.exercise_use_case.ExerciseUseCases
import com.croshapss.pushharder.feature_train.domain.use_case.trainingExercise_use_case.TrainingExerciseUseCases
import com.croshapss.pushharder.feature_train.domain.use_case.training_use_case.TrainingUseCases
import com.croshapss.pushharder.feature_train.presentation.trainingExercises.TrainingExerciseWithSets
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val trainingUseCases: TrainingUseCases,
    val exerciseUseCases: ExerciseUseCases,
    private val trainingExerciseUseCases: TrainingExerciseUseCases,
    private val exerciseSetUseCases: ExerciseSetUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(StatsState())
    val state: StateFlow<StatsState> = _state

    private val _trainingExercisesWithSets = MutableStateFlow<Map<String, List<TrainingExerciseWithSets>>>(emptyMap())

    private val _selectedTrainingId = MutableStateFlow<String?>(null)
    val selectedTrainingId: StateFlow<String?> = _selectedTrainingId

    init {
        loadAllExercises()
        loadAllTrainings()
    }

    fun onEvent(event: StatsEvent) {
        when (event) {
            is StatsEvent.SelectExercise -> {
                _state.update { it.copy(selectedExerciseName = event.exerciseName) }
                filterTrainingsByExercise(event.exerciseName) // Call filter method here
            }
        }
    }

    private fun loadAllExercises() {
        viewModelScope.launch {
            exerciseUseCases.getAllExercises().collect { exercises ->
                _state.update { it.copy(allExercises = exercises) }
            }
        }
    }

    private fun loadAllTrainings() {
        viewModelScope.launch {
            trainingUseCases.getAllTrainings()
                .onEach { trainings ->
                    _state.update { it.copy(filteredTrainings = trainings) }
                    trainings.forEach { training ->
                        loadTrainingExercisesWithSets(training.id)
                    }
                    if (trainings.isNotEmpty() && _state.value.selectedExerciseName == null) {
                        selectFirstExerciseFromTrainings(trainings)
                    }
                }
                .launchIn(viewModelScope)
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
                // Update the map with the new list of exercises for the specific training
                val updatedMap = _trainingExercisesWithSets.value.toMutableMap()
                updatedMap[trainingId] = list
                _trainingExercisesWithSets.update { updatedMap }
            }
        }
    }

    fun filterTrainingsByExercise(exerciseName: String) {
        viewModelScope.launch {
            val allTrainings = trainingUseCases.getAllTrainings().firstOrNull() ?: emptyList()
            val filteredTrainings = allTrainings.filter { training ->
                _trainingExercisesWithSets.value[training.id]?.any { it.exercise?.name == exerciseName } ?: false
            }

            _state.update {
                it.copy(
                    filteredTrainings = filteredTrainings,
                    trainingExercisesWithSets = _trainingExercisesWithSets.value
                )
            }

            // Automatically select the latest training with the selected exercise
            val latestTrainingWithExercise = filteredTrainings.maxByOrNull { it.date }
            _selectedTrainingId.value = latestTrainingWithExercise?.id
        }
    }

    fun updateSelectedTraining(trainingId: String) {
        _selectedTrainingId.value = trainingId
    }

    private fun selectFirstExerciseFromTrainings(trainings: List<Training>) {
        val firstExerciseName = trainings.firstNotNullOfOrNull { training ->
            _trainingExercisesWithSets.value[training.id]?.firstOrNull()?.exercise?.name
        }
        firstExerciseName?.let { onEvent(StatsEvent.SelectExercise(it)) }
    }
}