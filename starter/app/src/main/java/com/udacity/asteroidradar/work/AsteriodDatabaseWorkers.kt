package com.udacity.asteroidradar.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getAsteriodDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

class RefreshAsteriodDatabaseWorker(val context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    companion object {
        val NAME = "ASTERIODS DB REFRESH WORKER"
    }

    override suspend fun doWork(): Result {
        val db = getAsteriodDatabase(context)
        val repo = AsteroidRepository(db)
        return try {
            repo.refreshAsteriods()
            Result.success()
        } catch (e: HttpException) {
            Log.e("RefreshAsteriods", "Unable to refresh asteriod db: ${e.message}")
            Result.retry()
        }
    }
}

class DeleteOldAsteriodsWorker(val context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    companion object {
        val NAME = "ASTERIODS CLEANER"
    }

    override suspend fun doWork(): Result {
        val db = getAsteriodDatabase(context)
        val repo = AsteroidRepository(db)
        return try {
            repo.deleteExpiredAsteriods()
            Result.success()
        } catch (e: Exception) {
            Log.e("DeleteAsteriod", "Unable to delete old asteriods: ${e.message}")
            Result.retry()
        }
    }

}