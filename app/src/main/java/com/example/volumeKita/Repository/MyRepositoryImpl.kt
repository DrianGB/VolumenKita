package com.example.volumeKita.Repository

import android.R.attr.streamType
import android.app.Application
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import androidx.core.content.ContextCompat
import com.example.volumeKita.Data.ModelVolume
import com.example.volumeKita.Database.Dao
import com.example.volumeKita.Domain.Repository.MyRepository
import com.example.volumeKita.Service.AudioService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MyRepositoryImpl @Inject constructor(
    private val dao: Dao,
    private val context: Application,
) : MyRepository{
    override var currentModelVolume: ModelVolume = ModelVolume()

    private val _modelVolume: MutableSharedFlow<ModelVolume> = MutableSharedFlow<ModelVolume>(replay = 1)
    override val modelVolume: SharedFlow<ModelVolume> = _modelVolume


    override fun setVolume(volume: Int,streamType: Int) {
        val intent = Intent(context, AudioService::class.java);
        intent.putExtra(AudioService.VOLUME,volume)
        intent.putExtra(AudioService.STREAM_TYPE, streamType)
        intent.action = AudioService.ACTION_SET_VOLUME
        ContextCompat.startForegroundService(context,intent)
    }

    override fun refreshVolume() {
        val intent = Intent(context, AudioService::class.java);
        intent.action = AudioService.ACTION_GET_ALL_VOLUME
        ContextCompat.startForegroundService(context,intent)
    }

    override suspend fun updateFromService(modelVolumeNow: ModelVolume) {
        currentModelVolume = modelVolumeNow
        _modelVolume.emit(modelVolumeNow)
    }
}