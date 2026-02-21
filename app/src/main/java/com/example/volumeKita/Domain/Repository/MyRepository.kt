package com.example.volumeKita.Domain.Repository

import com.example.volumeKita.Data.ModelVolume
import kotlinx.coroutines.flow.SharedFlow

interface MyRepository{
    var currentModelVolume: ModelVolume
    val modelVolume: SharedFlow<ModelVolume>
    fun setVolume(volume: Int,streamType: Int);
    fun refreshVolume();
    suspend fun updateFromService(modelVolumeNow: ModelVolume)
}