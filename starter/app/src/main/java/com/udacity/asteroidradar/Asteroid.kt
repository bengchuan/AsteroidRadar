package com.udacity.asteroidradar

import android.os.Parcelable
import com.udacity.asteroidradar.database.DatabaseAsteroid
import kotlinx.android.parcel.Parcelize

/**
 * Note: we model the Network Model to Domain Model.
 */
@Parcelize
data class Asteroid(
    val id: Long, val codename: String,
    val closeApproachDate: String,
    val closeApproachDateEpoch: Long,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean,
    var isFavorite: Boolean
) : Parcelable {
    fun asDatabaseModel(): DatabaseAsteroid {
        return DatabaseAsteroid(
            id = id,
            codename = codename,
            closeApproachDate = closeApproachDate,
            closeApproachDateEpoch = closeApproachDateEpoch,
            absoluteMagnitude = absoluteMagnitude,
            estimatedDiameter = estimatedDiameter,
            relativeVelocity = relativeVelocity,
            distanceFromEarth = distanceFromEarth,
            isPotentiallyHazardous = isPotentiallyHazardous,
            isFavorite = isFavorite
        )
    }
}

//Extend the network/app model to database model
fun List<Asteroid>.asDatabaseModel(): List<DatabaseAsteroid> {
    return map {
        it.asDatabaseModel()
    }
}

