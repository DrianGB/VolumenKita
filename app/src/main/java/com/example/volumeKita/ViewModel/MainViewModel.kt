package com.example.volumeKita.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.volumeKita.Domain.Repository.MyRepository
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
}