package com.example.volumeKita.Repository

import com.example.volumeKita.Database.Dao
import com.example.volumeKita.Domain.Repository.MyRepository
import javax.inject.Inject

class MyRepositoryImpl @Inject constructor(
    private val dao: Dao
) : MyRepository{
    

    override fun setVolume(volume: Int) {
        TODO("Not yet implemented")
    }

    override fun refreshVolume() {
        TODO("Not yet implemented")
    }
}