package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.asDatabaseModel
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDatabase) {
    private val TAG = "AsteroidRepository"

    val asteriods: LiveData<List<Asteroid>> =
        Transformations.map(database.asteriodDao.getAsteriods()) {
            it.asDomainModel()
        }

    // Get Asteriods for next 7 days
    suspend fun refreshAsteriods() {
        withContext(Dispatchers.IO) {
            try {
                var result = NasaApi.nasaApiService.getNearEarthObjects(apiKey = API_KEY)
                val parsedResult = parseAsteroidsJsonResult(JSONObject(result))
                database.asteriodDao.insertAll(parsedResult.asDatabaseModel())
            } catch (e: Exception) {
                Log.w(TAG, e.printStackTrace().toString())
                throw  e
            }
        }
    }

    // Cleaning up Asteriods
    suspend fun deleteExpiredAsteriods() {
        withContext(Dispatchers.IO) {
            val instant = Clock.System.now()
            database.asteriodDao.deleteAsteriodsOlderThan(instant.toEpochMilliseconds())
        }
    }
}