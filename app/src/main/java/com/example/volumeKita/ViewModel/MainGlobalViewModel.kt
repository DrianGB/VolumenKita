package com.example.volumeKita.ViewModel

import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.example.volumeKita.Event.EventMainGlobal
import com.example.volumeKita.State.MainGlobalState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainGlobalViewModel
    @Inject constructor(
    ): ViewModel() {
    private val _state: MutableStateFlow<MainGlobalState> = MutableStateFlow(MainGlobalState())
    val state: StateFlow<MainGlobalState> = _state

    fun onEvent(event: EventMainGlobal){
        when(event){
            EventMainGlobal.MoreClick -> {

            }
        }
    }
}