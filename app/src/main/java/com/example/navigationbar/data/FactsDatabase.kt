package com.example.navigationbar.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Fact::class],
    version = 1
)
abstract class FactsDatabase : RoomDatabase() {
    abstract val dao: FactDao
}