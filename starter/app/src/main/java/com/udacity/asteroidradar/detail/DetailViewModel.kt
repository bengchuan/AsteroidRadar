package com.udacity.asteroidradar.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.getAsteriodDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class DetailViewModel(selectedAsteroid: Asteroid, application: Application) :
    AndroidViewModel(application) {
    // Near Earth Objects
    private val db = getAsteriodDatabase(application)
    private val asteroidRepo = AsteroidRepository(db)

    private val _asteriod = MutableLiveData<Asteroid>()
    val asteroid: LiveData<Asteroid>
        get() = _asteriod

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean>
        get() = _isFavorite

    init {
        _asteriod.value = selectedAsteroid
        _isFavorite.value = selectedAsteroid.isFavorite
    }

    fun updateAsteriod() {
        // toggle the value
        val value = !(_isFavorite.value!!)
        _isFavorite.value = value
        _asteriod.value?.isFavorite = value

        viewModelScope.launch {
            asteroidRepo.updateAsteriod(_asteriod.value!!)
        }
    }
}