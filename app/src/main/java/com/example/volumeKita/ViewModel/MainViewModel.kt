package com.example.volumeKita.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        viewModelScope.launch {
            repo.modelVolume.collect { newVolume ->
                _uiState.update {
                    it.copy(
                        soundMusic = (newVolume.music / newVolume.maxMusic.toFloat()).toFixed(2),
                        soundAlarm = (newVolume.alarm / newVolume.maxAlarm.toFloat()).toFixed(2),
                        soundNotification = (newVolume.notification / newVolume.maxNotification.toFloat()).toFixed(2),
                    )
                }
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
                
            }
            EventMain.onIconNotification -> {

            }
            is EventMain.onTouchMusic -> {

            }
            is EventMain.onTouchAlarm -> {

            }
            is EventMain.onTouchNotification -> {

            }
        }
    }
}