package com.example.navigationbar.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Fact(
    val date: String,
    val description: String,
    val month: Int,
    val day: Int,

    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
