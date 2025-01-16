//package com.example.apptest.feature_train.presentation.trainings
//
//import android.util.Log
//import androidx.compose.runtime.State
//import androidx.compose.runtime.mutableStateOf
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.apptest.feature_train.domain.model.Exercise
//import com.example.apptest.feature_train.domain.model.ExerciseSet
//import com.example.apptest.feature_train.domain.model.Training
//import com.example.apptest.feature_train.domain.model.TrainingExercise
//import com.example.apptest.feature_train.domain.use_case.training_use_case.TrainingUseCases
//import com.example.apptest.feature_train.domain.util.OrderType
//import com.example.apptest.feature_train.domain.util.TrainingOrder
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.flow.launchIn
//import kotlinx.coroutines.flow.onEach
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//import com.example.apptest.feature_train.domain.use_case.exercise_set_use_cases.ExerciseSetUseCases
//import com.example.apptest.feature_train.domain.use_case.exercise_use_case.ExerciseUseCases
//import com.example.apptest.feature_train.domain.use_case.trainingExercise_use_case.TrainingExerciseUseCases
//import com.example.apptest.feature_train.presentation.trainingExercises.TrainingExerciseWithSets
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.firstOrNull
//
//@HiltViewModel
//class TrainingsViewModel @Inject constructor(
//    private val trainingUseCases: TrainingUseCases,
//    private val trainingExerciseUseCases: TrainingExerciseUseCases,
//    val exerciseUseCases: ExerciseUseCases,
//    val exerciseSetUseCases: ExerciseSetUseCases
//) : ViewModel() {
//
//    private var trainingToDelete: Training? = null
//    private var deleteTrainingJob: Job? = null
//    private var getTrainingsJob: Job? = null
//
//    private val _state = mutableStateOf(TrainingsState())
//    val state: State<TrainingsState> = _state
//        // Log when the state is updated
//        get() {
//            Log.d("TrainingsViewModel", "State updated: $field")
//            return field
//        }
//
//    private val _trainingExercisesWithSets = MutableStateFlow<Map<String, List<TrainingExerciseWithSets>>>(emptyMap())
//    val trainingExercisesWithSets: StateFlow<Map<String, List<TrainingExerciseWithSets>>> = _trainingExercisesWithSets.asStateFlow()
//    // Log when trainingExercisesWithSets is updated
////        get() {
////            Log.d("TrainingsViewModel", "_trainingExercisesWithSets updated: ${field.entries.joinToString { "TrainingId: ${it.key} has ${it.value.size} exercises" }}")
////            return field
////        }
//
//    init {
//        Log.i("TrainingsViewModel", "Initialization started")
//        // getTrainings(TrainingOrder.Date(OrderType.Descending))  <-- COMMENT THIS OUT
//
//        // HARDCODED DATA FOR TESTING
//        viewModelScope.launch {
//            _state.value = state.value.copy(trainings = listOf(
//                Training(title = "Training 1", date = 123, id = "training1"),
//                Training(title = "Training 2", date = 456, id = "training2")
//            ))
//
//            _trainingExercisesWithSets.value = mapOf(
//                "training1" to listOf(
//                    TrainingExerciseWithSets(
//                        trainingExercise = TrainingExercise(trainingId = "training1", exerciseId = 1, id = "te1"),
//                        exercise = Exercise(name = "Push-ups", id = 1),
//                        sets = listOf(
//                            ExerciseSet(trainingExerciseId = "te1", setNumber = 1, reps = 10),
//                            ExerciseSet(trainingExerciseId = "te1", setNumber = 2, reps = 8)
//                        )
//                    ),
//                    TrainingExerciseWithSets(
//                        trainingExercise = TrainingExercise(trainingId = "training1", exerciseId = 2, id = "te2"),
//                        exercise = Exercise(name = "Pull-ups", id = 2),
//                        sets = listOf(
//                            ExerciseSet(trainingExerciseId = "te2", setNumber = 1, reps = 5),
//                        )
//                    )
//                ),
//                "training2" to listOf(
//                    TrainingExerciseWithSets(
//                        trainingExercise = TrainingExercise(trainingId = "training2", exerciseId = 3, id = "te3"),
//                        exercise = Exercise(name = "Squats", id = 3),
//                        sets = listOf(
//                            ExerciseSet(trainingExerciseId = "te3", setNumber = 1, reps = 15),
//                            ExerciseSet(trainingExerciseId = "te3", setNumber = 2, reps = 12),
//                            ExerciseSet(trainingExerciseId = "te3", setNumber = 3, reps = 10)
//                        )
//                    )
//                )
//            )
//        }
//        // Observe changes to the trainings list and load exercises when it updates
//        viewModelScope.launch {
//            state.value.trainings.let { trainings ->
//                Log.d("TrainingsViewModel", "Observed trainings list during initialization: ${trainings.map { it.title }}")
//                if (trainings.isNotEmpty()) {
//                    loadAllTrainingExercisesWithSets() //This does nothing because of hardcoded data
//                }
//            }
//        }
//        Log.i("TrainingsViewModel", "Initialization finished")
//    }
//
//    fun onEvent(event: TrainingsEvent) {
//        Log.d("TrainingsViewModel", "Received event: $event")
//        when (event) {
//            is TrainingsEvent.Order -> {
//                Log.d("TrainingsViewModel", "Ordering trainings by: ${event.trainingOrder}")
//                if (state.value.trainingOrder::class == event.trainingOrder::class &&
//                    state.value.trainingOrder.orderType == event.trainingOrder.orderType
//                ) {
//                    Log.d("TrainingsViewModel", "Order is the same, ignoring.")
//                    return
//                }
//                getTrainings(event.trainingOrder)
//            }
//
//            is TrainingsEvent.DeleteTraining -> {
//                Log.d("TrainingsViewModel", "Deleting training: ${event.training.title}")
//                trainingToDelete = event.training
//                // Update UI immediately
//                _state.value = state.value.copy(
//                    trainings = state.value.trainings.filter { it.id != event.training.id }
//                )
//                Log.d("TrainingsViewModel", "Training removed from state for immediate UI update.")
//                // Schedule actual deletion
//                viewModelScope.launch {
//                    deleteTrainingJob?.cancel()
//                    deleteTrainingJob = launch {
//                        Log.d("TrainingsViewModel", "Delaying deletion of training for 4 seconds...")
//                        kotlinx.coroutines.delay(4000L)
//                        trainingToDelete?.let { training ->
//                            Log.i("TrainingsViewModel", "Actually deleting training: ${training.title}")
//                            trainingUseCases.deleteTraining(training)
//                            trainingToDelete = null
//                            Log.d("TrainingsViewModel", "Training deletion complete.")
//                        } ?: Log.w("TrainingsViewModel", "trainingToDelete is null, cannot delete.")
//                    }
//                }
//            }
//
//            is TrainingsEvent.RestoreTraining -> {
//                Log.d("TrainingsViewModel", "Restoring training.")
//                deleteTrainingJob?.cancel()
//                trainingToDelete?.let { training ->
//                    Log.i("TrainingsViewModel", "Restoring training: ${training.title}")
//                    // Restore UI immediately
//                    _state.value = state.value.copy(
//                        trainings = state.value.trainings + training
//                    )
//                    Log.d("TrainingsViewModel", "Training restored to state.")
//                    trainingToDelete = null
//                } ?: Log.w("TrainingsViewModel", "No training to restore.")
//            }
//
//            is TrainingsEvent.ToggleOrderSection -> {
//                Log.d("TrainingsViewModel", "Toggling order section, current visibility: ${state.value.isOrderSectionVisible}")
//                _state.value = state.value.copy(
//                    isOrderSectionVisible = !state.value.isOrderSectionVisible
//                )
//                Log.d("TrainingsViewModel", "Order section visibility updated to: ${state.value.isOrderSectionVisible}")
//            }
//        }
//    }
//
//    private fun getTrainings(trainingOrder: TrainingOrder) {
//        Log.i("TrainingsViewModel", "Fetching all trainings ordered by: $trainingOrder")
//        getTrainingsJob?.cancel()
//        getTrainingsJob = trainingUseCases.getAllTrainings(trainingOrder)
//            .onEach { trainings ->
//                Log.d("TrainingsViewModel", "Trainings loaded from DB: ${trainings.map { it.title }}")
//                _state.value = state.value.copy(
//                    trainings = trainings,
//                    trainingOrder = trainingOrder
//                )
//                Log.d("TrainingsViewModel", "Trainings state updated.")
//                if (trainings.isNotEmpty()) {
//                    loadAllTrainingExercisesWithSets()
//                } else {
//                    Log.d("TrainingsViewModel", "No trainings found, skipping loadAllTrainingExercisesWithSets.")
//                }
//            }
//            .launchIn(viewModelScope)
//    }
//
//    private fun loadAllTrainingExercisesWithSets() {
//        Log.i("TrainingsViewModel", "Loading all training exercises with sets.")
//        viewModelScope.launch {
//            val tempMap = mutableMapOf<String, List<TrainingExerciseWithSets>>()
//            Log.d("TrainingsViewModel", "Iterating through trainings: ${state.value.trainings.map { it.title }}")
//            _state.value.trainings.forEach { training ->
//                Log.d("TrainingsViewModel", "Fetching exercises for training: ${training.title}, id: ${training.id}")
//                trainingExerciseUseCases.getExercisesForTraining(training.id).collect { trainingExercises ->
//                    Log.d("TrainingsViewModel", "Exercises for ${training.title} loaded: ${trainingExercises.map { it.id }}")
//                    val list = trainingExercises.map { trainingExercise ->
//                        Log.d("TrainingsViewModel", "Fetching exercise and sets for trainingExercise: ${trainingExercise.id}")
//                        val exercise = exerciseUseCases.getExerciseById(trainingExercise.exerciseId).firstOrNull()
//                        val sets = exerciseSetUseCases.getSetsForTrainingExercise(trainingExercise.id).firstOrNull().orEmpty()
//                        Log.d("TrainingsViewModel", "Exercise ${exercise?.name} and ${sets.size} sets loaded for trainingExercise ${trainingExercise.id}")
//                        TrainingExerciseWithSets(
//                            trainingExercise = trainingExercise,
//                            exercise = exercise,
//                            sets = sets
//                        )
//                    }
//                    tempMap[training.id] = list
//                    Log.d("TrainingsViewModel", "TrainingExercisesWithSets for ${training.title} loaded: ${list.size} exercises. IDs: ${list.joinToString { it.trainingExercise.id }}")
//
//                }
//            }
//            _trainingExercisesWithSets.value = tempMap
//            Log.d("TrainingsViewModel", "Retrieved _trainingExercisesWithSets: ${tempMap.entries.joinToString { "TrainingId: ${it.key} has ${it.value.size} exercises" }}")
//        }
//    }
//
//    fun getTrainingExercisesWithSets(trainingId: String): List<TrainingExerciseWithSets> {
//        Log.d("TrainingsViewModel", "getTrainingExercisesWithSets called for trainingId: $trainingId")
//        val exercises =  _trainingExercisesWithSets.value[trainingId] ?: emptyList()
//        Log.d("TrainingsViewModel", "Exercises returned for trainingId: $trainingId, count: ${exercises.size}")
//        return exercises
//    }
//}