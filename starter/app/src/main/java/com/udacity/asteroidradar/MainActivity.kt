package com.udacity.asteroidradar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.udacity.asteroidradar.work.DeleteOldAsteriodsWorker
import com.udacity.asteroidradar.work.RefreshAsteriodDatabaseWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // schedule asteriod db request
        scheduleTasks()
    }

    private fun scheduleTasks() {
        applicationScope.launch {
            val refreshTask = createRefreshTask()
            WorkManager.getInstance().enqueueUniquePeriodicWork(
                refreshTask.first,
                ExistingPeriodicWorkPolicy.KEEP,
                refreshTask.second
            )

            val deleteTask = createDeleteTask()
            WorkManager.getInstance().enqueueUniquePeriodicWork(
                deleteTask.first,
                ExistingPeriodicWorkPolicy.KEEP,
                deleteTask.second
            )
        }
    }

    // Not sure this is the right way to initialise
    private fun createRefreshTask() : Pair<String, PeriodicWorkRequest> {
        val refreshConstraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val request = PeriodicWorkRequestBuilder<RefreshAsteriodDatabaseWorker>(1, TimeUnit.DAYS)
            .setConstraints(refreshConstraints)
            .build()

        return Pair(RefreshAsteriodDatabaseWorker.NAME, request)
    }

    private fun createDeleteTask() : Pair<String, PeriodicWorkRequest> {
        val deleteConstraints =
            Constraints.Builder().setRequiresCharging(true).build()

        // Delete Old Asteriod
        val deleteRequest = PeriodicWorkRequestBuilder<DeleteOldAsteriodsWorker>(
            1,
            TimeUnit.DAYS
        ).setConstraints(deleteConstraints).build()

        return Pair(DeleteOldAsteriodsWorker.NAME, deleteRequest)
    }
}
