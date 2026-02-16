package com.example.volumeKita.Service

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AudioService: Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {
        val ACTION_SET_VOLUME = "set_volume"
        val ACTION_GET_VOLUME = "get_volume"

        val VOLUME = "volume"
        val STREAM_TYPE = "stream_type"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val audioManager: AudioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        when(intent?.action){
            ACTION_SET_VOLUME -> {
                val volume = intent.getIntExtra(VOLUME,0)
                val streamType = intent.getIntExtra(STREAM_TYPE,AudioManager.STREAM_MUSIC)
                audioManager.setStreamVolume(
                    streamType,
                    volume,
                    AudioManager.FLAG_SHOW_UI
                )
            }
            ACTION_GET_VOLUME -> {

            }
        }

        return START_NOT_STICKY
    }
}