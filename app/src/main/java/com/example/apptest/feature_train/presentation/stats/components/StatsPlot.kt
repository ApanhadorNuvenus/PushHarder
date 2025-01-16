package com.example.apptest.feature_train.presentation.stats.components

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.apptest.feature_train.domain.model.Training
import com.example.apptest.feature_train.presentation.trainingExercises.TrainingExerciseWithSets
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.renderer.LineChartRenderer
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun StatsPlot(
    trainings: List<Training>,
    selectedExerciseName: String?,
    trainingExercisesWithSets: Map<String, List<TrainingExerciseWithSets>>,
    onTrainingSelected: (Training?) -> Unit, // Callback for training selection
    backgroundColor: Color = MaterialTheme.colorScheme.surface // Default value
) {
    val chartEntries = remember(trainings, selectedExerciseName, trainingExercisesWithSets) {
        trainings.mapIndexedNotNull { index, training ->
            val totalReps = trainingExercisesWithSets[training.id]
                ?.firstOrNull { it.exercise?.name == selectedExerciseName }
                ?.sets
                ?.sumOf { it.reps ?: 0 } ?: 0

            if (totalReps > 0) {
                Entry(index.toFloat(), totalReps.toFloat(), training)
            } else {
                null
            }
        }
    }

    val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
    val dataSet = remember(chartEntries) {
        LineDataSet(chartEntries, "Trainings").apply {
            color = primaryColor
            lineWidth = 2f
            setDrawCircles(false)
            setDrawValues(false)
        }
    }

    val lineData = remember(dataSet) {
        LineData(dataSet)
    }

    val xAxisLabelPositions = remember(chartEntries) {
        chartEntries.map { it.x }
    }

    val xAxisLabels = remember(trainings) {
        trainings.map {
            SimpleDateFormat("dd/MM", Locale.getDefault()).format(Date(it.date))
        }
    }

    val chartModifier = Modifier
        .fillMaxWidth()
        .height(300.dp)
        .clipToBounds()

    if (chartEntries.isNotEmpty()) {
        AndroidView(
            modifier = chartModifier,
            factory = { context ->
                LineChart(context).apply {
                    val customRenderer = object : LineChartRenderer(this, animator, viewPortHandler) {
                        override fun drawExtras(c: android.graphics.Canvas?) {
                            super.drawExtras(c)
                            drawCirclesAndText(c)
                        }

                        private fun drawCirclesAndText(c: android.graphics.Canvas?) {
                            if (c == null) return
                            val circleRadius = 25f // Radius in pixels
                            val circlePaint = Paint().apply {
                                color = primaryColor
                                style = Paint.Style.FILL
                            }
                            val textPaint = Paint().apply {
                                color = Color.White.toArgb()
                                textSize = 30f // Text size in pixels
                                textAlign = Paint.Align.CENTER
                                typeface = Typeface.DEFAULT_BOLD // Make text bold
                                isAntiAlias = true
                            }

                            for (dataSet in mChart.lineData.dataSets) {
                                if (dataSet is LineDataSet && dataSet.isVisible) {
                                    for (i in 0 until dataSet.entryCount) {
                                        val entry = dataSet.getEntryForIndex(i)
                                        val point = floatArrayOf(entry.x, entry.y)
                                        mChart.getTransformer(dataSet.axisDependency).pointValuesToPixel(point)

                                        // Calculate text bounds
                                        val repsText = entry.y.roundToInt().toString()
                                        val textWidth = textPaint.measureText(repsText)
                                        val textBounds = android.graphics.Rect()
                                        textPaint.getTextBounds(repsText, 0, repsText.length, textBounds)

                                        // Adjust text position to center
                                        val textX = point[0]
                                        val textY = point[1] + textBounds.height() / 2f

                                        // Draw circle and text
                                        c.drawCircle(point[0], point[1], circleRadius, circlePaint)
                                        c.drawText(repsText, textX, textY, textPaint)
                                    }
                                }
                            }
                        }
                    }

                    renderer = customRenderer

                    // Chart configuration
                    data = lineData
                    description.isEnabled = false
                    xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.setDrawGridLines(false)
                    xAxis.granularity = 1f // Ensure at least one label is shown
                    xAxis.setLabelCount(xAxisLabels.size, false) // Force all labels to be shown

                    axisLeft.setDrawGridLines(false)
                    axisRight.isEnabled = false
                    legend.isEnabled = false
                    setTouchEnabled(true)
                    setPinchZoom(true)
                    isDragEnabled = true
                    setScaleEnabled(true)

                    // Set the value selected listener
                    setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                        override fun onValueSelected(e: Entry?, h: Highlight?) {
                            val training = e?.data as? Training
                            training?.let { onTrainingSelected(it) }
                        }

                        override fun onNothingSelected() {
                            onTrainingSelected(null) // Or handle deselection as needed
                        }
                    })
                    setBackgroundColor(backgroundColor.toArgb())
                }
            },
            update = { lineChart ->
                // Adjust X-Axis label positions after the chart has rendered
                lineChart.xAxis.valueFormatter = object : IndexAxisValueFormatter(xAxisLabels) {
                    override fun getFormattedValue(value: Float): String {
                        // Find the closest actual x-value (entry position)
                        val closestIndex = xAxisLabelPositions.minByOrNull { kotlin.math.abs(it - value) } ?: value
                        val index = chartEntries.indexOfFirst { it.x == closestIndex }.takeIf { it >= 0 } ?: return ""
                        return xAxisLabels.getOrNull(index) ?: ""
                    }
                }

                lineChart.data = lineData
                lineChart.notifyDataSetChanged()
                lineChart.invalidate()
            }
        )
    } else {
        Box(modifier = chartModifier, contentAlignment = Alignment.Center) {
            Text("No data available for plotting.")
        }
    }
}