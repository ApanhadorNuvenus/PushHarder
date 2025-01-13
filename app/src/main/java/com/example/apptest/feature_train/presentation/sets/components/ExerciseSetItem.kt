package com.example.apptest.feature_train.presentation.sets.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ExerciseSetItem(
    sets: Int?,
    reps: Int?,
    onDelete: (() -> Unit)? = null // Optional delete functionality
) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text("${sets ?: 0} x ${reps ?: 0}", style = MaterialTheme.typography.bodySmall)
        onDelete?.let { // Display delete button if onDelete is provided
            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}
