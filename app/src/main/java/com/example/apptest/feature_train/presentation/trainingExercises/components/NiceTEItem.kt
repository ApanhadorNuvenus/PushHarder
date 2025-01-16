package com.example.apptest.feature_train.presentation.trainings.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.apptest.feature_train.domain.model.Exercise
import com.example.apptest.feature_train.domain.model.ExerciseSet

@Composable
fun NiceTEItem(
    exercise: Exercise?,
    sets: List<ExerciseSet>,
    modifier: Modifier = Modifier,
    barColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    val totalReps = sets.sumOf { (it.reps ?: 0) * it.setNumber  }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                drawRoundRect(
                    color = barColor,
                    cornerRadius = CornerRadius(8.dp.toPx())
                )
            }
        ,
        contentAlignment = Alignment.Center
    ) {
        if (exercise != null)
            Text(
                text = "${exercise.name} - $totalReps",
                color = textColor,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            ) else {
            Text(
                text = "Exercise N/A - $totalReps",
                color = textColor,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}