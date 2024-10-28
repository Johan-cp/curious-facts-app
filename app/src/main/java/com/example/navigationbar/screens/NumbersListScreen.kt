package com.example.navigationbar.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.navigationbar.presentation.FactState
import com.example.navigationbar.presentation.FactsEvents

@Composable
fun NumbersListScreen(state: FactState, onEvent: (FactsEvents) -> Unit) {

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 26.dp)
    ) {
        Text(
            text = "Favorites",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            fontWeight = FontWeight.Bold
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {

            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(state.facts.size) { index ->
                    TodoFavItem(state = state, index = index, onEvent = onEvent)
                }
            }
        }
    }
}


@Composable
fun TodoFavItem(state: FactState, index: Int, onEvent: (FactsEvents) -> Unit) {
    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = state.facts[index].date,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )



            }
            Row {
                Text(
                    text = state.facts[index].description,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


