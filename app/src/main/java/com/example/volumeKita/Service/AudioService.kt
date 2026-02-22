package com.example.volumeKita.Service

import android.R.attr.streamType
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.database.ContentObserver
import android.media.AudioManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.volumeKita.Data.ModelVolume
import com.example.volumeKita.Domain.Repository.MyRepository
import com.example.volumeKita.R
import com.example.volumeKita.Screen.Settings
import com.example.volumeKita.ui.theme.MainActivity
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
        val ACTION_GET_DETECTED = "get_detected"

        val VOLUME = "volume"
        val STREAM_TYPE = "stream_type"

        val CHANNEL_ID = "volume_changing"
    }
    @Inject
    lateinit var mainRepo: MyRepository
    val serviceCoroutineIO = CoroutineScope(Dispatchers.IO)

    lateinit var observer: ContentObserver;
    private var lastVolume: ModelVolume? = null
    override fun onCreate() {
        super.onCreate()
        val audioManager: AudioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        observer = object : ContentObserver(Handler(Looper.getMainLooper())){
            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
                if(isSetInside){
                    isSetInside = false;
                    return
                }
                val modelVolume: ModelVolume = ModelVolume();

                var streamType: Int = AudioManager.STREAM_MUSIC
                modelVolume.music = audioManager.getStreamVolume(streamType)
                modelVolume.maxMusic = audioManager.getStreamMaxVolume(streamType)

                streamType = AudioManager.STREAM_ALARM
                modelVolume.alarm = audioManager.getStreamVolume(streamType)
                modelVolume.maxAlarm = audioManager.getStreamMaxVolume(streamType)

                streamType = AudioManager.STREAM_NOTIFICATION
                modelVolume.notification = audioManager.getStreamVolume(streamType)
                modelVolume.maxNotification = audioManager.getStreamMaxVolume(streamType)

                if(lastVolume == modelVolume){
                    return
                }
                serviceCoroutineIO.launch {
                    mainRepo.updateFromService(
                        modelVolumeNow = modelVolume
                    )
                }
                lastVolume = modelVolume
            }
        }

        applicationContext.contentResolver.registerContentObserver(
        Settings.System.CONTENT_URI, // apa yang di amati
        true, // amati dengan folder anak nya
        observer, // siapa pengamat nya
        )
    }

    var isSetInside: Boolean = false
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val audioManager: AudioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        val notificationEmergency = createNotification()
        startForeground(1,notificationEmergency)

        when(intent?.action){
            ACTION_SET_VOLUME -> {
                val volume = intent.getIntExtra(VOLUME,0)
                val streamType = intent.getIntExtra(STREAM_TYPE,AudioManager.STREAM_MUSIC)
                val maxVolumeType = audioManager.getStreamMaxVolume(streamType)
                isSetInside = true;
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
            ACTION_GET_DETECTED -> {

            }
        }

        return START_NOT_STICKY
    }
    fun createNotification(): Notification{
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "SEVICE KONTROL VOLUME",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }

        val intentMyApp: Intent = Intent(this, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,0,intentMyApp,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle("Volume Changer")
            .setContentText("Volume is Changing")
            .setSmallIcon(R.drawable.my_logo)
            .setContentIntent(pendingIntent)
            .build()
    }

    override fun onDestroy() {
        serviceCoroutineIO.cancel()
        applicationContext.contentResolver.unregisterContentObserver(observer)
        super.onDestroy()
    }

    fun UpdateService(streamType: Int,volume: Int,maxVolumeType : Int){
        serviceCoroutineIO.launch {
            when(streamType){
                AudioManager.STREAM_MUSIC -> {
                    mainRepo.updateFromService(
                        modelVolumeNow = mainRepo.currentModelVolume.copy().also {
                            it.music = volume
                            it.maxMusic = maxVolumeType
                        }
                    )
                }
                AudioManager.STREAM_ALARM -> {
                    mainRepo.updateFromService(
                        modelVolumeNow = mainRepo.currentModelVolume.copy().also {
                            it.alarm = volume
                            it.maxAlarm = maxVolumeType
                        }
                    )
                }
                AudioManager.STREAM_NOTIFICATION -> {
                    mainRepo.updateFromService(
                        modelVolumeNow = mainRepo.currentModelVolume.copy().also {
                            it.notification = volume
                            it.maxNotification = maxVolumeType
                        }
                    )
                }
            }
        }

    }
}