package com.example.navigationbar.presentation

import com.example.navigationbar.data.Fact

sealed interface FactsEvents {
    data class DeleteFact(val fact: Fact) : FactsEvents

    data class SaveFact(
        val date: String,
        val description: String,
        val month: Int,
        val day: Int
    ) : FactsEvents
}