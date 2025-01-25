//package com.example.apptest.feature_train.presentation.trainingExercises.components
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import com.example.apptest.feature_train.domain.use_case.exercise_use_case.ExerciseUseCases
//import com.example.apptest.feature_train.presentation.trainingExercises.TrainingExerciseWithSets
//
//@Composable
//fun TrainingExerciseList(
//    trainingExercisesWithSets: List<TrainingExerciseWithSets>,
//    exerciseUseCases: ExerciseUseCases = hiltViewModel(),
//    maxItemsToShow: Int = Int.MAX_VALUE // Show all by default
//) {
//    val itemsToShow = trainingExercisesWithSets.take(maxItemsToShow)
//    Column {
//        itemsToShow.forEach { item ->
//            TrainingExerciseItem(
//                trainingExercise = item.trainingExercise,
//                sets = item.sets,
//                exercise = item.exercise,
//                exerciseUseCases = exerciseUseCases,
//            )
//            Spacer(modifier = Modifier.height(4.dp)) // Reduced spacing
//        }
//    }
//}