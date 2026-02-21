package com.example.volumeKita.Event

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize

sealed class EventMain {
    data class onTouchMusic(var size: IntSize, var offsetClicked: Offset) : EventMain()
    data class onUpTouchMusic(var size: IntSize, var offsetClicked: Offset) : EventMain()
    data class onTouchAlarm(var size: IntSize, var offsetClicked: Offset) : EventMain()
    data class onUpTouchAlarm(var size: IntSize, var offsetClicked: Offset) : EventMain()
    data class onTouchNotification(var size: IntSize, var offsetClicked: Offset) : EventMain()
    data class onUpTouchNotification(var size: IntSize, var offsetClicked: Offset) : EventMain()

    object onIconMusic : EventMain();
    object onIconAlarm : EventMain();
    object onIconNotification : EventMain();
}