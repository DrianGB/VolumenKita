package com.example.volumeKita.Domain.Repository

interface MyRepository{
    fun setVolume(volume: Int);
    fun refreshVolume();
}