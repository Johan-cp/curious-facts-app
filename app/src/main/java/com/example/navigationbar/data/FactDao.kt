package com.example.navigationbar.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FactDao {

    @Upsert
    suspend fun upsertFact(fact: Fact)

    @Delete
    suspend fun deleteFact(fact: Fact)

    @Query("SELECT * FROM fact ORDER BY month ASC, day ASC")
    fun getFacts(): Flow<List<Fact>>


}