package com.example.volumeKita.Event

import androidx.compose.ui.unit.IntSize

sealed class EventMain {
    data class onTouchMusic(var detect: IntSize) : EventMain()
    data class onTouchAlarm(var detect: IntSize) : EventMain()
    data class onTouchNotification(var detect: IntSize) : EventMain()

    object onIconMusic : EventMain();
    object onIconAlarm : EventMain();
    object onIconNotification : EventMain();
}