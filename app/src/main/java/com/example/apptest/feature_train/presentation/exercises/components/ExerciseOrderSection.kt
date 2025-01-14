//package com.example.apptest.feature_train.presentation.exercises.components
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.width
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import com.example.apptest.feature_train.domain.util.ExerciseOrder
//import com.example.apptest.feature_train.domain.util.OrderType
//import com.example.apptest.feature_train.presentation.trainings.components.DefaultRadioButton
//
//@Composable
//fun ExerciseOrderSection(
//    modifier: Modifier = Modifier,
//    exerciseOrder: ExerciseOrder = ExerciseOrder.Type(OrderType.Descending), // Default order
//    onOrderChange: (ExerciseOrder) -> Unit
//) {
//    Column(
//        modifier = modifier
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            DefaultRadioButton(
//                text = "Title",
//                selected = exerciseOrder is ExerciseOrder.Name,
//                onSelect = { onOrderChange(ExerciseOrder.Name(exerciseOrder.orderType)) }
//            )
//            Spacer(modifier = Modifier.width(8.dp))
////            DefaultRadioButton(
////                text = "Type",
////                selected = exerciseOrder is ExerciseOrder.Type,
////                onSelect = { onOrderChange(ExerciseOrder.Type(exerciseOrder.orderType)) }
////            )
//        }
//        Spacer(modifier = Modifier.width(16.dp))
//        Row(
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            DefaultRadioButton(
//                text = "Ascending",
//                selected = exerciseOrder.orderType is OrderType.Ascending,
//                onSelect = {
//                    onOrderChange(exerciseOrder.copy(OrderType.Ascending))
//                }
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            DefaultRadioButton(
//                text = "Descending",
//                selected = exerciseOrder.orderType is OrderType.Descending,
//                onSelect = {
//                    onOrderChange(exerciseOrder.copy(OrderType.Descending))
//                }
//            )
//        }
//    }
//}