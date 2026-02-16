package com.example.volumeKita.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.volumeKita.Database.Entities.ScheduleVolume

@Dao
interface Dao{
    @Insert
    fun InsertSchedule(schedule: ScheduleVolume);
    @Delete
    fun DeleteSchedule(schedule: ScheduleVolume);
    @Update
    fun UpdateSchedule(schedule: ScheduleVolume);
    @Query("SELECT * FROM schedule")
    fun GetAllSchedule(): List<ScheduleVolume>;
}