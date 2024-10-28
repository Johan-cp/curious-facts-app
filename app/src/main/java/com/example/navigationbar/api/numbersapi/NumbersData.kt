package com.example.navigationbar.api.numbersapi

data class Month(
    val id: Int,
    val month: String,
    val days: Int
)

object NumbersData {
    val monthsOfYear = listOf(
        Month(id = 0, month = "January", days = 31),
        Month(id = 1, month = "February", days = 29),
        Month(id = 2, month = "March", days = 31),
        Month(id = 3, month = "April", days = 30),
        Month(id = 4, month = "May", days = 31),
        Month(id = 5, month = "June", days = 30),
        Month(id = 6, month = "July", days = 31),
        Month(id = 7, month = "August", days = 31),
        Month(id = 8, month = "September", days = 30),
        Month(id = 9, month = "October", days = 31),
        Month(id = 10, month = "November", days = 30),
        Month(id = 11, month = "December", days = 31)
    )
}