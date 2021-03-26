package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.*
import androidx.room.Room.databaseBuilder

@Dao
interface AsteroidDao {

    // Delete Asteriods worker should remove the expired asteriods. We don't need to compare closeApproachDate
    @Query("SELECT * FROM databaseAsteroid ORDER BY closeApproachDate DESC")
    fun getAsteriods(): List<DatabaseAsteroid>

    @Query("SELECT * FROM databaseAsteroid WHERE closeApproachDate == :currentDate ORDER BY closeApproachDate DESC")
    fun getAsteriodsByToday(currentDate: String): List<DatabaseAsteroid>

    @Query("SELECT * FROM databaseAsteroid WHERE isFavorite == 1")
    fun getFavoriteAsteriods(): List<DatabaseAsteroid>

    @Query("DELETE from databaseAsteroid WHERE closeApproachDateEpoch < :currentDate")
    fun deleteAsteriodsOlderThan(currentDate: Long)

    @Update
    fun updateAsteriod(databaseAstroid: DatabaseAsteroid)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(databaseAstroid: List<DatabaseAsteroid>)
}

@Database(entities = [DatabaseAsteroid::class], version = 3, exportSchema = false)
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