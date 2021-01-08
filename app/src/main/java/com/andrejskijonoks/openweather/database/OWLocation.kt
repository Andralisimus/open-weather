package com.andrejskijonoks.openweather.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Location")
data class OWLocation(
    @PrimaryKey
    val name: String,
    val latitude: Double,
    val longitude: Double,
)


