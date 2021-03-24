package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Room.databaseBuilder

@Dao
interface AsteroidDao {
    @Query("SELECT * FROM databaseAsteroid ORDER BY closeApproachDate DESC")
    fun getAsteriods(): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM databaseAsteroid WHERE closeApproachDate >= :currentDate ORDER BY closeApproachDate DESC")
    fun getAsteriodsByWeek(currentDate: Long): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM databaseAsteroid WHERE closeApproachDate == :currentDate ORDER BY closeApproachDate DESC")
    fun getAsteriodsByToday(currentDate: Long): LiveData<List<DatabaseAsteroid>>

    @Query("DELETE from databaseAsteroid WHERE closeApproachDate < :currentDate")
    fun deleteAsteriodsOlderThan(currentDate: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(databaseAstroid: List<DatabaseAsteroid>)
}

@Database(entities = [DatabaseAsteroid::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteriodDao: AsteroidDao
}

private lateinit var DB_INSTANCE: AsteroidDatabase

fun getAsteriodDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::DB_INSTANCE.isInitialized) {
            DB_INSTANCE = databaseBuilder(
                context.applicationContext,
                AsteroidDatabase::class.java,
                "Asteriods"
            ).build()
        }
    }
    return DB_INSTANCE
}