// com.example.apptest.feature_train.presentation.util
package com.croshapss.pushharder.feature_train.presentation.util

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object EventBus {
    private val _events = MutableSharedFlow<Event>()
    val events: SharedFlow<Event> = _events.asSharedFlow()

    suspend fun emitEvent(event: Event) {
        _events.emit(event)
    }
}

sealed class Event {
    object ExerciseUpdated : Event()
}