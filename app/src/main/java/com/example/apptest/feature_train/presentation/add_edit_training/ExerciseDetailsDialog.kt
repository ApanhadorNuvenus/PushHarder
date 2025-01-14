//package com.example.apptest.feature_train.presentation.add_edit_training
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.AlertDialog
//import androidx.compose.material3.Button
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.text.input.KeyboardType
//import com.example.apptest.feature_train.domain.model.Exercise
//import com.example.apptest.feature_train.domain.model.ExerciseType
//
//@Composable
//fun ExerciseDetailsDialog(
//    exercise: Exercise,
//    onDismiss: () -> Unit,
//    onConfirm: (Int?, Int?) -> Unit
//) {
//    var reps by remember { mutableStateOf("") }
//    var duration by remember { mutableStateOf("") }
//
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text("Enter Details for ${exercise.name}") },
//        text = {
//            Column {
//                when (exercise.exerciseType) {
//                    is ExerciseType.Reps -> {
//                        OutlinedTextField(
//                            value = reps,
//                            onValueChange = { newValue ->
//                                reps = if (newValue.all { it.isDigit() }) newValue else reps
//                            },
//                            label = { Text("Reps") },
//                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//                        )
//                    }
//                    is ExerciseType.Duration -> {
//                        OutlinedTextField(
//                            value = duration,
//                            onValueChange = { newValue ->
//                                duration = if (newValue.all { it.isDigit() }) newValue else duration
//                            },
//                            label = { Text("Duration (in seconds)") },
//                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//                        )
//                    }
//                }
//            }
//        },
//        confirmButton = {
//            Button(onClick = {
//                onConfirm(reps.toIntOrNull(), duration.toIntOrNull())
//            }) {
//                Text("Confirm")
//            }
//        },
//        dismissButton = {
//            Button(onClick = onDismiss) {
//                Text("Cancel")
//            }
//        }
//    )
//}