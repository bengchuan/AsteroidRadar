package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.Constants.SUPPORTED_MEDIA_TYPE
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.database.getAsteriodDatabase
import com.udacity.asteroidradar.repository.AsteriodFilter
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

enum class PictureOfTheDayImageStatus { LOADING, ERROR, UNSUPPORTED_MEDIA_TYPE }
class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "MainViewModel"

    // Picture of the day
    private val _picOfTheDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _picOfTheDay

    private val _picOfTheDayCaption = MutableLiveData<String>()
    val pictureOfDayCaption: LiveData<String>
        get() = _picOfTheDayCaption

    private val _picOfTheDayErrorImageholder = MutableLiveData<Int>()
    val pictureOfDayImageHolder: LiveData<Int>
        get() = _picOfTheDayErrorImageholder


    val showUnsupportedMediaTypeErrorMessage = Transformations.map(pictureOfDay) {
        if (!it.mediaType.toLowerCase().trim().equals(SUPPORTED_MEDIA_TYPE)) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private val _asteriods = MutableLiveData<List<Asteroid>>()
    val asteriods: LiveData<List<Asteroid>>
        get() = _asteriods

    // Near Earth Objects
    private val db = getAsteriodDatabase(application)
    private val asteroidRepo = AsteroidRepository(db)


    init {
        getPictureOfTheDay()
        getAsteriodsFilteredBy(AsteriodFilter.ALL)
    }


    fun getAsteriodsFilteredBy(filter: AsteriodFilter) {
        viewModelScope.launch {
            _asteriods.value = asteroidRepo.getAsteriodsBy(filter)
        }
    }

    /**
     * GET picture of the day
     */
    private fun getPictureOfTheDay() {
        viewModelScope.launch {
            try {
                //TODO: figure out a way to not having api_key as query param
                val result = NasaApi.nasaApiService.getPictureOfTheDay(apiKey = API_KEY)
                _picOfTheDay.value = result
                _picOfTheDayCaption.value = result.title
            } catch (e: Exception) {
                Log.e(
                    TAG,
                    "Getting error calling GetPictureOfTheDay:\n ${e.printStackTrace()}"
                )
                _picOfTheDayErrorImageholder.value = R.drawable.ic_broken_image
                _picOfTheDayCaption.value = R.string.error_unable_to_load_pod.toString()
            }
        }
    }
}