package com.crashapss.pushharder.feature_train.presentation.stats.components

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.crashapss.pushharder.feature_train.domain.model.Exercise
import com.crashapss.pushharder.feature_train.domain.model.Training
import com.crashapss.pushharder.feature_train.presentation.trainingExercises.TrainingExerciseWithSets
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
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
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun StatsPlot(
    trainings: List<Training>,
    selectedExerciseName: String?,
    trainingExercisesWithSets: Map<String, List<TrainingExerciseWithSets>>,
    onTrainingSelected: (Training?) -> Unit,
    allExercises: List<Exercise>
) {
    val selectedExerciseGoal = allExercises.firstOrNull { it.name == selectedExerciseName }?.goal?.toFloat() ?: 0f
    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val axisColor = MaterialTheme.colorScheme.onSurface.toArgb()

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

    val dataSet = remember(chartEntries) {
        LineDataSet(chartEntries, "Trainings").apply {
            color = primaryColor.toArgb()
            lineWidth = 4f // Increased line width
            setDrawCircles(false)
            setDrawValues(false)
            this.isHighlightEnabled = true
            highLightColor = primaryColor.toArgb()
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

    // Use remember with a key that depends on the theme to recreate the chart
    val lineChartKey = MaterialTheme.colorScheme
    val lineChart = remember(lineChartKey) { mutableStateOf<LineChart?>(null) }

    if (chartEntries.isNotEmpty()) {
        Box(modifier = chartModifier) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    LineChart(context).apply {
                        lineChart.value = this // Store the LineChart instance

                        // Custom Renderer - Define it HERE in factory for initialization
                        renderer = object : LineChartRenderer(this, animator, viewPortHandler) {
                            override fun drawExtras(c: android.graphics.Canvas?) {
                                super.drawExtras(c)
                                drawCirclesAndText(c)
                                drawGoalLine(c)
                            }

                            private fun drawCirclesAndText(c: android.graphics.Canvas?) {
                                if (c == null) return
                                val circleRadius = 25f // Radius in pixels
                                val circlePaint = Paint().apply {
                                    color = primaryColor.toArgb()
                                    style = Paint.Style.FILL
                                }
                                val textPaint = Paint().apply {
                                    color = onPrimaryColor.toArgb()
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

                            private fun drawGoalLine(c: android.graphics.Canvas?) {
                                if (selectedExerciseGoal <= 0) return

                                val yAxis = mChart.getAxis(YAxis.AxisDependency.LEFT)
                                val goalLinePixel = mChart.getTransformer(YAxis.AxisDependency.LEFT)
                                    .getPixelForValues(0f, selectedExerciseGoal)

                                val textPaint = Paint().apply {
                                    color = Color.Red.toArgb()
                                    textSize = 35f
                                    textAlign = Paint.Align.LEFT
                                    isAntiAlias = true
                                    typeface = Typeface.DEFAULT_BOLD
                                }

                                if (!goalLinePixel.y.isNaN() && !goalLinePixel.y.isInfinite()) {
                                    val linePaint = Paint().apply {
                                        color = Color.Red.toArgb()
                                        strokeWidth = 8f
                                        style = Paint.Style.STROKE
                                    }

                                    // Convert goalLinePixel.y (Double) to Float for drawLine and drawText
                                    val goalLineY = goalLinePixel.y.toFloat()

                                    c?.drawLine(0f, goalLineY, mChart.width.toFloat(), goalLineY, linePaint)

                                    val text = "Goal: ${selectedExerciseGoal.toInt()}"
                                    val textX = 16f
                                    val textY = goalLineY - 16f // Use goalLineY (Float) for textY

                                    c?.drawText(text, textX, textY, textPaint)
                                }
                            }
                        }

                        // Chart configuration - Configure initial chart settings here in factory
                        data = lineData

                        description.isEnabled = false // Disable description
                        legend.isEnabled = false      // Disable legend

                        xAxis.apply {
                            valueFormatter = IndexAxisValueFormatter(xAxisLabels)
                            position = XAxis.XAxisPosition.BOTTOM
                            setDrawGridLines(false)
                            granularity = 1f
                            setLabelCount(xAxisLabels.size, false)
                            textColor = axisColor
                            axisLineColor = axisColor
                        }

                        axisLeft.apply {
                            setDrawGridLines(false)
                            textColor = axisColor
                            axisLineColor = axisColor
                            axisMinimum = 0f // Ensure Y-axis starts at 0
                        }

                        axisRight.isEnabled = false // Disable right Y-axis

                        setTouchEnabled(true)      // Enable touch gestures
                        setPinchZoom(false)      // Disable pinch zoom
                        isDragEnabled = true        // Enable dragging
                        setScaleEnabled(false)     // Disable scaling

                        // Set value selected listener
                        setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                            override fun onValueSelected(e: Entry?, h: Highlight?) {
                                val training = e?.data as? Training
                                training?.let { onTrainingSelected(it) }
                            }
                            override fun onNothingSelected() {
                                onTrainingSelected(null)
                            }
                        })
                    }
                },
                update = { chart ->
                    // Update chart data and labels - Keep updates minimal
                    chart.data = lineData

                    // Update X-Axis labels in update block in case trainings list changes
                    chart.xAxis.valueFormatter = object : IndexAxisValueFormatter(xAxisLabels) {
                        override fun getFormattedValue(value: Float): String {
                            val closestIndex = xAxisLabelPositions.minByOrNull { kotlin.math.abs(it - value) } ?: value
                            val index = chartEntries.indexOfFirst { it.x == closestIndex }.takeIf { it >= 0 } ?: return ""
                            return xAxisLabels.getOrNull(index) ?: ""
                        }
                    }

                    // Calculate Y-axis range based on goal and data - Recalculate on update
                    val maxValue = max(
                        chart.data.yMax,
                        selectedExerciseGoal * 1.1f // Add 10% margin to goal
                    )
                    chart.axisLeft.axisMaximum = maxValue


                    chart.xAxis.textColor = axisColor // Ensure axis colors are updated on theme change
                    chart.axisLeft.textColor = axisColor
                    chart.xAxis.axisLineColor = axisColor
                    chart.axisLeft.axisLineColor = axisColor

                    chart.notifyDataSetChanged() // Notify chart to refresh data
                    chart.invalidate()          // Invalidate the chart to redraw
                }
            )

            // REMOVE Canvas block - Goal line is drawn in LineChartRenderer now
        }
    } else {
        Box(modifier = chartModifier, contentAlignment = Alignment.Center) {
            Text("No data available for plotting.")
        }
    }
}