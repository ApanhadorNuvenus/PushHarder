package com.example.apptest.feature_train.presentation.trainings.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.apptest.feature_train.domain.model.Training
import com.example.apptest.feature_train.presentation.trainingExercises.components.TrainingExercisesList
import java.text.DateFormat

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TrainingItem(
    training: Training,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    var showQuickView by remember { mutableStateOf(false) }
    var showDropdownMenu by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .combinedClickable(
                onClick = { showQuickView = true },
                onLongClick = { showDropdownMenu = true }
            )
    ) {

        Box(
            modifier = modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = { showQuickView = true },
                    onLongClick = { showDropdownMenu = true }
                )
        ) {
            // Main content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = training.title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = DateFormat.getDateInstance().format(training.date),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                if (training.failure) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Failure",
                            tint = Color.Red
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Failure", color = Color.Red)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Weights: ${training.weights ?: "N/A"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                ////// TRAINING EXERCISES OF THE TRAINING ////////
                Box(
                    modifier = Modifier
                        .weight(1f) // Take remaining space
                        .fillMaxWidth()
                ) {
                    TrainingExercisesList(trainingId = training.id)
                }
            }

            // Delete button
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete training"
                )
            }

            // Dropdown menu
            DropdownMenu(
                expanded = showDropdownMenu,
                onDismissRequest = { showDropdownMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Edit") },
                    onClick = {
                        onEditClick()
                        showDropdownMenu = false
                    }
                )
            }

            //
            // QUICK VIEW DIALOG //
            //
//            if (showQuickView) {
//                Dialog(
//                    onDismissRequest = { showQuickView = false },
//                    properties = DialogProperties(usePlatformDefaultWidth = false)
//                ) {
//                    Surface(
//                        modifier = Modifier
//                            .fillMaxWidth(0.9f)
//                            .fillMaxHeight(0.8f),
//                        shape = MaterialTheme.shapes.large,
//                        tonalElevation = 8.dp
//                    ) {
//                        Column(
//                            modifier = Modifier
//                                .padding(16.dp)
//                        ) {
//                            // Header content (fixed)
//                            Column {
//                                Text(
//                                    text = training.title,
//                                    style = MaterialTheme.typography.headlineMedium
//                                )
//                                Text(
//                                    text = DateFormat.getDateInstance().format(training.date),
//                                    style = MaterialTheme.typography.bodyLarge
//                                )
//                                if (training.failure) {
//                                    Text("Failed Training", color = Color.Red)
//                                }
//                                Text("Weights: ${training.weights ?: "N/A"}")
//                                Spacer(modifier = Modifier.height(16.dp))
//                                Text(
//                                    "Exercises:",
//                                    style = MaterialTheme.typography.titleMedium
//                                )
//                            }
//
//                            // Scrollable content (exercises list)
//                            Box(
//                                modifier = Modifier
//                                    .weight(1f) // Take remaining space
//                                    .fillMaxWidth()
//                            ) {
//                                TrainingExercisesList(trainingId = training.id)
//                            }
//
//                            // Footer (fixed)
//                            Button(
//                                onClick = { showQuickView = false },
//                                modifier = Modifier
//                                    .align(Alignment.End)
//                                    .padding(top = 8.dp)
//                            ) {
//                                Text("Close")
//                            }
//                        }
//                    }
//                }
//            }
        }
    }
}