package com.crashapss.pushharder.feature_train.presentation.add_edit_training.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.crashapss.pushharder.feature_train.domain.model.ExerciseSet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSetDialog(
    onDismiss: () -> Unit,
    onConfirm: (ExerciseSet) -> Unit
) {
    var setNumberText by remember { mutableStateOf("") }
    var repsText by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Set") },
        text = {
            Column {
                Row(modifier = Modifier.fillMaxWidth()) {
                    // Set Number
                    OutlinedTextField(
                        value = setNumberText,
                        onValueChange = { newValue ->
                            setNumberText = newValue.filter { it.isDigit() }
                            showError = false
                        },
                        label = { Text("Set") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = showError && setNumberText.isNotEmpty() && repsText.isEmpty()
                    )

                    Spacer(modifier = Modifier.padding(4.dp))

                    // x
                    Text(
                        text = "x",
                        modifier = Modifier.padding(top = 16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.padding(4.dp))

                    // Reps
                    OutlinedTextField(
                        value = repsText,
                        onValueChange = { newValue ->
                            repsText = newValue.filter { it.isDigit() }
                            showError = false
                        },
                        label = { Text("Reps") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = showError && setNumberText.isNotEmpty() && repsText.isEmpty()
                    )
                }
                if (showError && setNumberText.isNotEmpty() && repsText.isEmpty()) {
                    Text(
                        text = "Reps are required if the set number is specified",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (setNumberText.isNotEmpty() && repsText.isEmpty()) {
                        showError = true
                    } else {
                        val setNumber = if (setNumberText.isEmpty()) 1 else setNumberText.toIntOrNull() ?: 1
                        val reps = if (repsText.isEmpty()) setNumberText.toIntOrNull() ?: 1 else repsText.toIntOrNull() ?: 1

                        val newSet = ExerciseSet(
                            trainingExerciseId = "", // This will be filled in later
                            setNumber = setNumber,
                            reps = reps
                        )
                        onConfirm(newSet)
                        onDismiss()
                    }
                },
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}