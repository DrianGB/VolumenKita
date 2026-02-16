package com.example.volumeKita.Database.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule")
data class ScheduleVolume(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var time: String = "00:00",
    var day: String = "1-1",
    var volume: Float = 0f
)