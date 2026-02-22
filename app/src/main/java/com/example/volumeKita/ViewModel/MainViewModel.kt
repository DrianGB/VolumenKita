package com.example.volumeKita.ViewModel

import android.media.AudioManager
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volumeKita.Data.ModelVolume
import com.example.volumeKita.Domain.Repository.MyRepository
import com.example.volumeKita.Event.EventMain
import com.example.volumeKita.R
import com.example.volumeKita.State.MainState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: MyRepository
): ViewModel(){

    private val _uiState: MutableStateFlow<MainState> = MutableStateFlow(MainState())
    var uiState: StateFlow<MainState> = _uiState

    var lastVolume: ModelVolume = ModelVolume()


    init {
        repo.refreshVolume()
        viewModelScope.launch {
            repo.modelVolume.collect { newVolume ->
                Log.d("masuk","Ini Masuk Ke sini viewModel lastVolume music: ${lastVolume.music} dan volume sekarang ${newVolume.music} dan volume repo ${repo.currentModelVolume.music}")

                // icon is mute or not
                lastVolume.music = UpdateModelVolumeCheck(lastVolume.music,repo.currentModelVolume.music)
                lastVolume.alarm = UpdateModelVolumeCheck(lastVolume.alarm,repo.currentModelVolume.alarm)
                lastVolume.notification = UpdateModelVolumeCheck(lastVolume.notification,repo.currentModelVolume.notification)


                _uiState.update {
                    it.copy(
                        soundMusic = (newVolume.music / newVolume.maxMusic.toFloat()).toFixed(2),
                        soundAlarm = (newVolume.alarm / newVolume.maxAlarm.toFloat()).toFixed(2),
                        soundNotification = (newVolume.notification / newVolume.maxNotification.toFloat()).toFixed(2),
                        maxSoundMusic = newVolume.maxMusic.toFloat(),
                        maxSoundAlarm = newVolume.maxAlarm.toFloat(),
                        maxSoundNotification = newVolume.maxNotification.toFloat(),
                        musicIconResource = checkZeroIcon(newVolume.music,R.drawable.sound,R.drawable.no_sound),
                        alarmIconResource = checkZeroIcon(newVolume.alarm,R.drawable.alarm,R.drawable.no_alarm),
                        notificationIconResource = checkZeroIcon(newVolume.notification,R.drawable.notification,R.drawable.no_notification)
                    )
                }
                repo.currentModelVolume = newVolume
                musicClicked = false
            }
        }
    }
    fun UpdateModelVolumeCheck(lastVolume: Int,currentModelVolume: Int): Int{
        return if(lastVolume != currentModelVolume) currentModelVolume else lastVolume
    }
    fun UpdateModelVolumeZeroCheck(newVolume: Int, currentVolume: Int): Int{
        return if(newVolume == 0) currentVolume else newVolume
    }

    fun OpenLoading(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true
                )
            }
        }
    }
    fun CloseLoading(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }
    fun SetVolume(volume: Int, streamType: Int){
        repo.setVolume(volume,streamType)
    }

    fun Float.toFixed(fixedTo: Int): Float{
        return (this * (Math.pow(10.0, fixedTo.toDouble()).toInt())).toInt() / (Math.pow(10.0,fixedTo.toDouble()).toFloat())
    }

    // event
    var musicClicked: Boolean = false;
    var musicClickedOffset: Long = 0L;
    var musicClickOffsetMax: Long = 200L
    fun onEvent(eventMain: EventMain){
        when(eventMain){
            EventMain.onIconMusic -> {
                viewModelScope.launch {
                    if(musicClicked == false && (musicClickedOffset < System.currentTimeMillis())){
                        musicClicked = true
                        if(_uiState.value.musicIconResource == R.drawable.sound){
                            SetVolume(0,AudioManager.STREAM_MUSIC)
                        }else {
                            SetVolume(lastVolume.music,AudioManager.STREAM_MUSIC)
                        }
                        musicClickedOffset = System.currentTimeMillis() + musicClickOffsetMax
                    }
                }
            }
            EventMain.onIconAlarm -> {
                viewModelScope.launch {
                    if(musicClicked == false && (musicClickedOffset < System.currentTimeMillis())){
                        musicClicked = true
                        if(_uiState.value.alarmIconResource == R.drawable.alarm){
                            SetVolume(0,AudioManager.STREAM_ALARM)
                        }else {
                            SetVolume(lastVolume.alarm,AudioManager.STREAM_ALARM)
                        }
                        musicClickedOffset = System.currentTimeMillis() + musicClickOffsetMax
                    }
                }
            }
            EventMain.onIconNotification -> {
                viewModelScope.launch {
                    if(musicClicked == false && (musicClickedOffset < System.currentTimeMillis())){
                        musicClicked = true
                        if(_uiState.value.notificationIconResource == R.drawable.notification){
                            SetVolume(0,AudioManager.STREAM_NOTIFICATION)
                        }else {
                            SetVolume(lastVolume.alarm,AudioManager.STREAM_NOTIFICATION)
                        }
                        musicClickedOffset = System.currentTimeMillis() + musicClickOffsetMax
                    }
                }
            }
            is EventMain.onTouchMusic -> {
                viewModelScope.launch {
                    val resultOnTouch = onTouchSound(size = eventMain.size, offsetClicked = eventMain.offsetClicked)
                    if(resultOnTouch >= 0){
                        SetVolume(
                            Math.ceil(resultOnTouch.coerceIn(0f,1f) * _uiState.value.maxSoundMusic.toDouble()).toInt(),
                            AudioManager.STREAM_MUSIC
                        )
                    }
                }
            }
            is EventMain.onUpTouchMusic -> {

            }
            is EventMain.onTouchAlarm -> {
                val resultOnTouch = onTouchSound(size = eventMain.size, offsetClicked = eventMain.offsetClicked)
                if(resultOnTouch >= 0){
                    SetVolume(
                            Math.ceil(resultOnTouch.coerceIn(0f,1f) * _uiState.value.maxSoundAlarm.toDouble()).toInt(),
                        AudioManager.STREAM_ALARM)
                }
            }
            is EventMain.onUpTouchAlarm -> {

            }
            is EventMain.onTouchNotification -> {
                val resultOnTouch = onTouchSound(size = eventMain.size, offsetClicked = eventMain.offsetClicked)
                if(resultOnTouch >= 0){
                    SetVolume(
                        Math.ceil(resultOnTouch.coerceIn(0f,1f) * _uiState.value.maxSoundMusic.toDouble()).toInt(),
                        AudioManager.STREAM_NOTIFICATION)
                }
            }
            is EventMain.onUpTouchNotification -> {

            }
        }
    }
    fun onTouchSound(size: IntSize, offsetClicked: Offset): Float{
        val offsetTargetXMin: Float = 5f
        val offsetTargetXMax: Float = size.width - 5f
        val offsetTargetY: Float = size.height / 2f

        val toleranceY : Float = 20f

        val positionX: Float = offsetClicked.x
        val positionY: Float = offsetClicked.y
        if(
            positionX > offsetTargetXMin && positionX < (size.width - offsetTargetXMin) &&
            positionY > offsetTargetY - toleranceY && positionY < offsetTargetY + toleranceY
        ){
            // IF position in target
            val percentData: Float = (positionX - 5) / (offsetTargetXMax - 5f)

            return percentData
        }
        return -1f;
    }

    // system

    fun checkZeroIcon(volume: Int, targetIconResource: Int,zeroTargetIconResource: Int): Int{
        return if(volume <= 0) zeroTargetIconResource else targetIconResource
    }

}