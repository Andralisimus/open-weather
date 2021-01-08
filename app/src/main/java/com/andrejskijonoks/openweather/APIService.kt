package com.andrejskijonoks.openweather

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("data/2.5/onecall?")
    suspend fun getWeatherByCords(
        @Query("lat") lat : String,
        @Query("lon") lon : String,
        @Query("appid") appid : String,
        @Query("units") units : String
    ): Response<OneCallResponse>

    companion object {

        fun getInstance(): APIService {
            val retrofit = Retrofit.Builder()

                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(APIService::class.java)
        }
    }
}