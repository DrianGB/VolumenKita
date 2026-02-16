package com.example.volumeKita.State

import com.example.volumeKita.R

data class MainState(
    var isLoading: Boolean = false,
    var soundIconResouce: Int = R.drawable.no_sound,
    var soundVolume: Float = 0.0f,
    var notificationIconResource: Int = R.drawable.notification,
    var soundNotification: Float = 0.0f
    )