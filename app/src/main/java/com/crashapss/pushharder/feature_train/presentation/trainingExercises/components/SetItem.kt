package com.crashapss.pushharder.feature_train.presentation.trainingExercises.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.sqrt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetItem(
    setNumber: Int,
    reps: Int,
    onDelete: () -> Unit
) {
    var offset by remember { mutableStateOf(Offset.Zero) }
    var isRemoved by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (isRemoved) 0f else 1f,
        animationSpec = tween(durationMillis = 300), finishedListener = {
            if (isRemoved) onDelete()
        }
    )

    val dragThreshold = 100.dp // Distance to drag before triggering delete

    if (!isRemoved) {
        Card(
            modifier = Modifier
                .padding(4.dp)
                .size(width = 80.dp, height = 40.dp)
                .offset { IntOffset(offset.x.toInt(), offset.y.toInt()) }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            val distance = sqrt(offset.x * offset.x + offset.y * offset.y)
                            if (distance > dragThreshold.toPx()) {
                                isRemoved = true
                            } else {
                                offset = Offset.Zero // Reset to original position
                            }
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            offset += dragAmount
                        }
                    )
                }
                .graphicsLayer {
                    this.alpha = alpha // Apply alpha for fade-out animation
                },
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val displayText = when {
                    setNumber == 1 -> "$reps"
                    reps == 1 -> "${setNumber}x$reps"
                    else -> "${setNumber}x$reps"
                }
                Text(
                    text = displayText,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}