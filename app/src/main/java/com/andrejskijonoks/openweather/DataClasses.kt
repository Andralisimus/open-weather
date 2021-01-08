package com.andrejskijonoks.openweather

import com.google.gson.annotations.SerializedName

data class OneCallResponse(
        val timezone: String,
        val current: Current,
        val hourly: List<Current>,
        val daily: List<Daily>
)

data class Current(
        val dt: Long,
        val temp: Double,
        val feels_like: Double,
        val humidity: Int,
        val clouds: Int,
        val wind_speed: Double,
        val uvi: Double,
        val pressure: Int,

        @SerializedName("weather")
        val weatherList: List<Weather>,
)

data class Weather(
    val description: String,
    val icon: String
)

data class Daily(
        val dt: Long,
        val temp: Temp,
        val feels_like: FeelsLike,
        val humidity: Int,
        val wind_speed: Double,
        val clouds: Int,

        @SerializedName("weather")
        val weatherList: List<Weather>,
)

data class Temp(
        val day: Double,
        val min: Double,
        val max: Double,
        val night: Double,
        val eve: Double,
        val morn: Double
)

data class FeelsLike(
        val day: Double,
        val night: Double,
        val eve: Double,
        val morn: Double
)