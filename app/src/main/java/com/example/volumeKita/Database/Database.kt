package com.example.volumeKita.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.volumeKita.Database.Entities.ScheduleVolume

@Database(
    entities = [ScheduleVolume::class],
    version = 1
)
abstract class Database : RoomDatabase(){
    abstract fun dao(): Dao;
}