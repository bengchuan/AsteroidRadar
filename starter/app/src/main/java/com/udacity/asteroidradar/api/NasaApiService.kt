package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Deals with https://api.nasa.gov
 */

// Json to Kotlin object
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


//https://api.nasa.gov/planetary/apod?api_key=<demo>
interface NasaApiService {
    @GET("planetary/apod")
    suspend fun getPictureOfTheDay(@Query("api_key") apiKey: String): PictureOfDay

    @GET("neo/rest/v1/feed")
    suspend fun getNearEarthObjects(
        @Query("api_key") apiKey: String
    ): String
}

object NasaApi {
    val nasaApiService: NasaApiService by lazy {
        retrofit.create(NasaApiService::class.java)
    }
}
