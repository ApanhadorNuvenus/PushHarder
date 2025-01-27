//package com.example.apptest.feature_train.data.repository
//
//import com.example.apptest.feature_train.domain.model.Training
//import com.example.apptest.feature_train.domain.repository.TrainingRepository
//import kotlinx.coroutines.flow.Flow
//
//import kotlinx.coroutines.flow.MutableStateFlow
//
//class FakeTrainingRepository : TrainingRepository {
//
//    private val trainings = MutableStateFlow<List<Training>>(
//        listOf(
//            Training("Training 1", 1672531200000, false, 50),
//            Training("Training 2", 1672617600000, true, 75),
//            Training("Training 3", 1672704000000, false, 60)
//        )
//    )
//
//    override fun getAllTrainings(): Flow<List<Training>> {
//        return trainings
//    }
//
//    override suspend fun getTrainingById(trainingId: Int): Training? {
//        return trainings.value.find { it.id == trainingId }
//    }
//
//    override suspend fun addTraining(training: Training) {
//        trainings.value = trainings.value + training
//    }
//
//    override suspend fun updateTraining(training: Training) {
//        trainings.value = trainings.value.map {
//            if (it.id == training.id) training else it
//        }
//    }
//
//    override suspend fun deleteTraining(training: Training) {
//        trainings.value = trainings.value.toMutableList().also { it.remove(training) }
//    }
//}