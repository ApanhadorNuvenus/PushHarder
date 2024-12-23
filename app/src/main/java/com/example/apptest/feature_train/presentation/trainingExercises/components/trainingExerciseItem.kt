package com.example.apptest.feature_train.presentation.trainingExercises.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.apptest.feature_train.domain.model.ExerciseType
import com.example.apptest.feature_train.domain.model.Training
import com.example.apptest.feature_train.domain.model.TrainingExercise


// In your add_edit_training package or a common components package
@Composable
fun TrainingExerciseItem(
    exerciseName: String,
    exerciseType: ExerciseType,
    reps: Int?,
    duration: Int?,
    onDelete: (() -> Unit)? = null // Optional delete functionality
) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(exerciseName, style = MaterialTheme.typography.bodyLarge)
        when (exerciseType) {
            is ExerciseType.Reps -> {
                reps?.let {
                    Text("Reps: $it", style = MaterialTheme.typography.bodyMedium)
                }
            }
            is ExerciseType.Duration -> {
                duration?.let {
                    Text("Duration: $it seconds", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
        onDelete?.let { // Display delete button if onDelete is provided
            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}