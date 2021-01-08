package com.andrejskijonoks.openweather.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(OWLocation::class), version = 1)
abstract class OWDatabase : RoomDatabase() {
    abstract fun owLocationDao() : OWLocationDao
}