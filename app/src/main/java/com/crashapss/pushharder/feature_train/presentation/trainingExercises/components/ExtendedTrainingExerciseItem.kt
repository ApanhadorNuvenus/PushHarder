package com.crashapss.pushharder.feature_train.presentation.trainingExercises.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.crashapss.pushharder.feature_train.domain.model.ExerciseSet
import com.crashapss.pushharder.feature_train.domain.model.TrainingExercise
import com.crashapss.pushharder.feature_train.domain.use_case.exercise_use_case.ExerciseUseCases
import com.crashapss.pushharder.feature_train.presentation.add_edit_training.components.TransparentHintTextField // Import

@Composable
fun ExtendedTrainingExerciseItem(
    trainingExercise: TrainingExercise,
    currentWeightInput: String,
    currentFailureState: Boolean,
    sets: List<ExerciseSet>,
    exerciseUseCases: ExerciseUseCases,
    modifier: Modifier = Modifier,
    onAddSet: () -> Unit = {},
    onDeleteSet: (ExerciseSet) -> Unit = {},
    onDeleteTrainingExercise: () -> Unit = {},
    onWeightChanged: (String, String) -> Unit,
    onFailureChanged: (String, Boolean) -> Unit
) {
    val exercise by exerciseUseCases.getExerciseById(trainingExercise.exerciseId).collectAsState(initial = null)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Training Exercise Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                exercise?.let {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row {
                    IconButton(onClick = onAddSet) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add set"
                        )
                    }
                    IconButton(onClick = onDeleteTrainingExercise) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete training exercise"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sets List (Horizontal Arrangement)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                sets.forEach { set ->
                    SetItem(
                        setNumber = set.setNumber ?: 1,
                        reps = set.reps ?: 0,
                        onDelete = { onDeleteSet(set) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- ADD WEIGHT INPUT ---
            TransparentHintTextField(
                text = currentWeightInput, // Display current weight from input state
                hint = "Weight (optional)",
                onValueChange = { weightValue ->
                    onWeightChanged(trainingExercise.id, weightValue) // Callback on weight change
                },
                onFocusChange = {  }, // Implement if needed
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(8.dp))

            // --- ADD FAILURE CHECKBOX ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = currentFailureState, // Use the state variable for immediate update
                    onCheckedChange = { isChecked ->
                        onFailureChanged(trainingExercise.id, isChecked)
                    }
                )
                Text(text = "Failure")
            }
        }
    }
}