package com.example.navigationbar.api.numbersapi

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get

class NumbersComponent {
    private val httpClient = HttpClient(Android)

    suspend fun getCurious(day: Int = 13, month: Int = 9): String {
        return httpClient.get("http://numbersapi.com/${month}/${day}/date")
    }
}