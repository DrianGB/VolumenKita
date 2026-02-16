package com.example.volumeKita.DI

import android.app.Application
import androidx.room.Room
import com.example.volumeKita.Database.Dao
import com.example.volumeKita.Database.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun ProvidesDao(
        app: Application
    ): Dao{
        val database = Room.databaseBuilder(
            context = app.applicationContext,
            klass = Database::class.java,
            name = "dataVolume.db"
        ).build()
        return database.dao()
    }
}