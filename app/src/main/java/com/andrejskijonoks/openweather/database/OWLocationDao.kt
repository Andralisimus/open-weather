package com.andrejskijonoks.openweather.database

import androidx.room.*

@Dao
interface OWLocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(owLocation: OWLocation)

    @Query("SELECT * FROM Location")
    suspend fun getLocations(): List<OWLocation>

    @Delete
    suspend fun delete(owLocation: OWLocation)
}