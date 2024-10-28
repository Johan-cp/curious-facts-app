package com.example.navigationbar.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import com.example.navigationbar.data.Fact

data class FactState(
    val facts: List<Fact> = emptyList(),

    val date: MutableState<String> = mutableStateOf(""),
    val description: MutableState<String> = mutableStateOf(""),
    val month: MutableState<Int> = mutableIntStateOf(0),
    val day: MutableState<Int> = mutableIntStateOf(0)
)
