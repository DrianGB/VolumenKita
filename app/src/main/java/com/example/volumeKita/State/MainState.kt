package com.example.volumeKita.State

import com.example.volumeKita.R

data class MainState(
    var isLoading: Boolean = false,
    var musicIconResource: Int = R.drawable.sound,
    var soundMusic: Float = 0.0f,
    var maxSoundMusic: Float = 0.0f,
    var notificationIconResource: Int = R.drawable.notification,
    var soundNotification: Float = 0.0f,
    var maxSoundNotification: Float = 0.0f,
    var alarmIconResource: Int = R.drawable.alarm,
    var soundAlarm: Float = 0.0f,
    var maxSoundAlarm: Float = 0.0f
    )