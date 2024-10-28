package com.example.navigationbar.presentation

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigationbar.data.Fact
import com.example.navigationbar.data.FactDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FactsViewModel(
    private val dao: FactDao
) : ViewModel() {

    private var facts = dao.getFacts()

    private val _state = MutableStateFlow(FactState())

    val state = combine(_state, facts) { state, facts ->
        state.copy(
            facts = facts,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FactState())

    fun onEvent(event: FactsEvents) {
        when (event) {
            is FactsEvents.DeleteFact -> {
                viewModelScope.launch {
                    dao.deleteFact(event.fact)
                }
            }

            is FactsEvents.SaveFact -> {
                val fact = Fact(
                    date = event.date,
                    month = event.month,
                    day = event.day,
                    description = event.description
                )

                viewModelScope.launch {
                    dao.upsertFact(fact)
                }

                _state.update {
                    it.copy(
                        date = mutableStateOf(""),
                        month = mutableIntStateOf(0),
                        day = mutableIntStateOf(0),
                        description = mutableStateOf("")
                    )
                }
            }


        }
    }


}