package com.crashapss.pushharder.feature_train.presentation.trainings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.crashapss.pushharder.feature_train.domain.util.OrderType
import com.crashapss.pushharder.feature_train.domain.util.TrainingOrder

@Composable
fun OrderSection(
    modifier: Modifier,
    trainingOrder: TrainingOrder = TrainingOrder.Date(OrderType.Descending),
    // when any button clicked emits new training order?...
    // to the screen, screen calls corresponding event TrainingsEvent.Order(it) with new order
    onOrderChange: (TrainingOrder) -> Unit
){
    Column (
        modifier = modifier
    ) {
        Row (
            modifier = Modifier.fillMaxWidth()
        ){
            DefaultRadioButton(
                text = "title",
                selected = trainingOrder is TrainingOrder.Title,
                onSelect = { onOrderChange(TrainingOrder.Title(trainingOrder.orderType))}
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Date",
                selected = trainingOrder is TrainingOrder.Date,
                onSelect = { onOrderChange(TrainingOrder.Date(trainingOrder.orderType))}
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row (
            modifier = Modifier.fillMaxWidth()
        ){
            DefaultRadioButton(
                text = "Ascending",
                selected = trainingOrder.orderType is OrderType.Ascending,
                onSelect = {onOrderChange(trainingOrder.copy(OrderType.Ascending))}
            )
            Spacer(modifier = Modifier.width(8.dp))
            DefaultRadioButton(
                text = "Descending",
                selected = trainingOrder.orderType is OrderType.Descending,
                onSelect = {onOrderChange (trainingOrder.copy(OrderType.Descending))}
            )
        }
    }
}