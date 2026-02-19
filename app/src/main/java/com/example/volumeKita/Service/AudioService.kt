package com.example.volumeKita.Service

import android.R.attr.streamType
import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.os.IBinder
import com.example.volumeKita.Data.ModelVolume
import com.example.volumeKita.Domain.Repository.MyRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AudioService: Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        val ACTION_SET_VOLUME = "set_volume"
        val ACTION_GET_VOLUME = "get_volume"
        val ACTION_GET_ALL_VOLUME = "get_all_volume"

        val VOLUME = "volume"
        val STREAM_TYPE = "stream_type"
    }
    @Inject
    lateinit var mainRepo: MyRepository
    val serviceCoroutineIO = CoroutineScope(Dispatchers.IO)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val audioManager: AudioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        when(intent?.action){
            ACTION_SET_VOLUME -> {
                val volume = intent.getIntExtra(VOLUME,0)
                val streamType = intent.getIntExtra(STREAM_TYPE,AudioManager.STREAM_MUSIC)
                val maxVolumeType = audioManager.getStreamMaxVolume(streamType)
                audioManager.setStreamVolume(
                    streamType,
                    volume,
                    AudioManager.FLAG_SHOW_UI
                )
                UpdateService(streamType = streamType,volume = volume, maxVolumeType = maxVolumeType)

            }
            ACTION_GET_VOLUME -> {
                val streamType = intent.getIntExtra(STREAM_TYPE, AudioManager.STREAM_MUSIC)
                val resultVolume = audioManager.getStreamVolume(streamType)
                val maxVolumeType = audioManager.getStreamMaxVolume(streamType)
                UpdateService(streamType = streamType,volume = resultVolume, maxVolumeType = maxVolumeType)
            }
            ACTION_GET_ALL_VOLUME -> {
                val modelVolume: ModelVolume = ModelVolume()
                var volumes = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                var maxVolumes = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                modelVolume.music = volumes
                modelVolume.maxMusic = maxVolumes
                volumes = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION)
                maxVolumes = audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION)
                modelVolume.notification = volumes
                modelVolume.maxNotification = maxVolumes
                volumes = audioManager.getStreamVolume(AudioManager.STREAM_ALARM)
                maxVolumes = audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION)
                modelVolume.alarm = volumes
                modelVolume.maxAlarm = maxVolumes
                serviceCoroutineIO.launch{
                    mainRepo.updateFromService(
                        modelVolumeNow = modelVolume
                    )
                }
            }
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        serviceCoroutineIO.cancel()
        super.onDestroy()
    }

    fun UpdateService(streamType: Int,volume: Int,maxVolumeType : Int){
        serviceCoroutineIO.launch {
            when(streamType){
                AudioManager.STREAM_MUSIC -> {
                    mainRepo.updateFromService(
                        modelVolumeNow = mainRepo.currentModelVolume.also {
                            it.music = volume
                            it.maxMusic = maxVolumeType
                        }
                    )
                }
                AudioManager.STREAM_ALARM -> {
                    mainRepo.updateFromService(
                        modelVolumeNow = mainRepo.currentModelVolume.also {
                            it.alarm = volume
                            it.maxAlarm = maxVolumeType
                        }
                    )
                }
                AudioManager.STREAM_NOTIFICATION -> {
                    mainRepo.updateFromService(
                        modelVolumeNow = mainRepo.currentModelVolume.also {
                            it.notification = volume
                            it.maxNotification = maxVolumeType
                        }
                    )
                }
            }
        }
    }
}