package com.udacity.asteroidradar.repository

import android.util.Log
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.asDatabaseModel
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


enum class AsteriodFilter { ALL, TODAY, FAVORITE }
class AsteroidRepository(private val database: AsteroidDatabase) {
    private val TAG = "AsteroidRepository"

    // Return Asteriod by Filter
    suspend fun getAsteriodsBy(filter: AsteriodFilter): List<Asteroid> {
        var result: List<Asteroid> = listOf()
        withContext(Dispatchers.IO) {
            result = when (filter) {
                AsteriodFilter.ALL -> database.asteriodDao.getAsteriods().asDomainModel()
                AsteriodFilter.FAVORITE -> database.asteriodDao.getFavoriteAsteriods()
                    .asDomainModel()
                AsteriodFilter.TODAY -> {
                    val today = LocalDateTime.now()
                    database.asteriodDao.getAsteriodsByToday(today.format(DateTimeFormatter.ISO_DATE))
                        .asDomainModel()
                }
            }
        }
        return result
    }

    suspend fun updateAsteriod(asteroid: Asteroid) {
        withContext(Dispatchers.IO) {
            database.asteriodDao.updateAsteriod(asteroid.asDatabaseModel())
        }
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
            val today = LocalDateTime.now()
            database.asteriodDao.deleteAsteriodsOlderThan(today.toEpochSecond(ZoneOffset.MIN))
        }
    }
}