package com.example.apptest.feature_train.presentation.stats.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import com.example.apptest.feature_train.domain.model.Exercise

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDropdown(
    exercises: List<Exercise>,
    selectedExerciseName: String?,
    onExerciseSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = true,
            value = selectedExerciseName ?: "Select Exercise",
            onValueChange = {},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = TextFieldDefaults.colors( // <---- Customized colors here
                focusedIndicatorColor = Color.Transparent, // Remove focused indicator
                unfocusedIndicatorColor = Color.Transparent, // Remove unfocused indicator
                disabledIndicatorColor = Color.Transparent, // Remove disabled indicator
                errorIndicatorColor = Color.Transparent, // Remove error indicator
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), // Subtle background
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), // Even more subtle when unfocused
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy( // Optional: Adjust text style
                color = MaterialTheme.colorScheme.onSurfaceVariant // Ensure text color is readable
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            exercises.forEach { exercise ->
                DropdownMenuItem(
                    text = {
                        Text(
                            exercise.name,
                            style = MaterialTheme.typography.bodyMedium // Optional: Adjust dropdown item text style
                        )
                    },
                    onClick = {
                        onExerciseSelected(exercise.name)
                        expanded = false
                    }
                )
            }
        }
    }
}