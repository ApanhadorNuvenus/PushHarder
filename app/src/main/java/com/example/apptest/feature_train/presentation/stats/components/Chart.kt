//package com.example.apptest.feature_train.presentation.stats.components
//
//import android.graphics.Color
//import android.util.Log
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.SideEffect
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.viewinterop.AndroidView
//import com.example.apptest.feature_train.presentation.stats.ChartEntryData
//import com.github.mikephil.charting.charts.BarChart
//import com.github.mikephil.charting.charts.HorizontalBarChart
//import com.github.mikephil.charting.components.AxisBase
//import com.github.mikephil.charting.components.XAxis
//import com.github.mikephil.charting.data.BarData
//import com.github.mikephil.charting.data.BarDataSet
//import com.github.mikephil.charting.data.BarEntry
//import com.github.mikephil.charting.data.Entry
//import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
//import com.github.mikephil.charting.formatter.ValueFormatter
//import com.github.mikephil.charting.highlight.Highlight
//import com.github.mikephil.charting.listener.OnChartValueSelectedListener
//import com.github.mikephil.charting.utils.ColorTemplate
//import androidx.compose.ui.graphics.Color as ComposeColor
//import androidx.compose.ui.graphics.toArgb
//import androidx.compose.ui.unit.dp
//
//@Composable
//fun TrainingChart(chartData: List<ChartEntryData>) {
//    Log.d("TrainingChart", "Chart data received: $chartData")
//
//    val maxReps = chartData.maxOfOrNull { it.reps } ?: 0
//
//    // Define color once and reuse
//    val barColor = ComposeColor(0xFF64B5F6).toArgb()
//
//    AndroidView(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(400.dp) // Fixed height for the chart
//            .background(MaterialTheme.colorScheme.surface),
//        factory = { context ->
//            HorizontalBarChart(context).apply {
//                // Chart configuration
//                setDrawBarShadow(false)
//                setDrawValueAboveBar(true)
//                description.isEnabled = false
//                setPinchZoom(false)
//                setDrawGridBackground(false)
//
//                // X-axis configuration
//                val xAxis = xAxis
//                xAxis.position = XAxis.XAxisPosition.BOTTOM
//                xAxis.setDrawGridLines(false)
//                xAxis.labelCount = chartData.size
//                xAxis.valueFormatter = object : IndexAxisValueFormatter() {
//                    override fun getFormattedValue(value: Float): String {
//                        return chartData.getOrNull(value.toInt())?.formattedDate ?: ""
//                    }
//                }
//
//                // Y-axis configuration
//                axisLeft.setDrawLabels(false)
//                axisLeft.setDrawGridLines(false)
//                axisLeft.axisMinimum = 0f
//                axisRight.isEnabled = false
//
//                // Other configurations
//                legend.isEnabled = false
//                setFitBars(true)
//            }
//        },
//        update = { chart ->
//            val entries = chartData.mapIndexed { index, data ->
//                BarEntry(index.toFloat(), data.reps.toFloat())
//            }
//            val dataSet = BarDataSet(entries, "Trainings").apply {
//                color = barColor // Use the defined color
//            }
//            val barData = BarData(dataSet).apply {
//                barWidth = 0.5f
//                setValueTextColor(Color.BLACK)
//                setValueTextSize(12f)
//                setValueFormatter(object : ValueFormatter() {
//                    override fun getFormattedValue(value: Float): String {
//                        return value.toInt().toString()
//                    }
//                })
//            }
//
//            chart.data = barData
//
//            // Refresh the chart
//            chart.notifyDataSetChanged()
//            chart.invalidate()
//        }
//    )
//
//    // Label for reps
//    Box(modifier = Modifier
//        .fillMaxWidth()
//        .padding(top = 8.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(text = "Reps", style = MaterialTheme.typography.bodyMedium)
//    }
//}