package com.example.volumeKita.ViewModel

import android.media.AudioManager
import android.util.Log
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

    init {
        repo.refreshVolume()
        viewModelScope.launch {
            repo.modelVolume.collect { newVolume ->
                _uiState.update {
                    it.copy(
                        soundMusic = (newVolume.music / newVolume.maxMusic.toFloat()).toFixed(2),
                        soundAlarm = (newVolume.alarm / newVolume.maxAlarm.toFloat()).toFixed(2),
                        soundNotification = (newVolume.notification / newVolume.maxNotification.toFloat()).toFixed(2),
                        maxSoundMusic = newVolume.maxMusic.toFloat(),
                        maxSoundAlarm = newVolume.maxAlarm.toFloat(),
                        maxSoundNotification = newVolume.maxNotification.toFloat()
                    )
                }
                repo.currentModelVolume = newVolume
            }
        }
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
    fun onEvent(eventMain: EventMain){
        when(eventMain){
            EventMain.onIconMusic -> {
                if(_uiState.value.musicIconResource == R.drawable.sound){
                    _uiState.update {
                        it.copy(
                            musicIconResource = R.drawable.no_sound
                        )
                    }
                }else {
                    _uiState.update {
                        it.copy(
                            musicIconResource = R.drawable.sound
                        )
                    }
                }
            }
            EventMain.onIconAlarm -> {
                if(_uiState.value.alarmIconResource == R.drawable.alarm){
                    _uiState.update {
                        it.copy(
                            alarmIconResource = R.drawable.no_alarm
                        )
                    }
                }else {
                    _uiState.update {
                        it.copy(
                            alarmIconResource = R.drawable.alarm
                        )
                    }
                }
            }
            EventMain.onIconNotification -> {
                if(_uiState.value.notificationIconResource == R.drawable.notification){
                    _uiState.update {
                        it.copy(
                            notificationIconResource = R.drawable.no_notification
                        )
                    }
                }else {
                    _uiState.update {
                        it.copy(
                            notificationIconResource = R.drawable.notification
                        )
                    }
                }
            }
            is EventMain.onTouchMusic -> {
                val resultOnTouch = onTouchSound(size = eventMain.size, offsetClicked = eventMain.offsetClicked)
                if(resultOnTouch >= 0){
                    SetVolume((resultOnTouch.coerceIn(0f,1f) * _uiState.value.maxSoundMusic).toInt(),
                        AudioManager.STREAM_MUSIC)
                }

            }
            is EventMain.onUpTouchMusic -> {

            }
            is EventMain.onTouchAlarm -> {
                val resultOnTouch = onTouchSound(size = eventMain.size, offsetClicked = eventMain.offsetClicked)
                if(resultOnTouch >= 0){
                    SetVolume((resultOnTouch.coerceIn(0f,1f) * _uiState.value.maxSoundAlarm).toInt(),
                        AudioManager.STREAM_ALARM)
                }
            }
            is EventMain.onUpTouchAlarm -> {

            }
            is EventMain.onTouchNotification -> {
                val resultOnTouch = onTouchSound(size = eventMain.size, offsetClicked = eventMain.offsetClicked)
                if(resultOnTouch >= 0){
                    SetVolume((resultOnTouch.coerceIn(0f,1f) * _uiState.value.maxSoundNotification).toInt(),
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

}