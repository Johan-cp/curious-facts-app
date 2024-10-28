package com.example.navigationbar.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.navigationbar.R
import com.example.navigationbar.api.numbersapi.NumbersComponent
import com.example.navigationbar.api.numbersapi.NumbersData
import com.example.navigationbar.presentation.FactState
import com.example.navigationbar.presentation.FactsEvents
import kotlinx.coroutines.launch

val monthsOfYear = NumbersData.monthsOfYear

@Composable
fun NumbersScreen(state: FactState, onEvent: (FactsEvents) -> Unit) {

    val scope = rememberCoroutineScope()
    var text by remember {
        mutableStateOf("Loading")
    }

    val openDialog = remember { mutableStateOf(false) }

    val month = remember { mutableIntStateOf(12) }
    val day = remember { mutableIntStateOf(26) }
    var refreshTrigger by remember { mutableStateOf(true) }

    var isTextVisible by remember { mutableStateOf(true) }

    var isFavorite by remember { mutableStateOf(false) }

    Text(
        text = "Curious Facts",
        style = MaterialTheme.typography.displayMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 26.dp)
            .padding(top = 32.dp),
        fontWeight = FontWeight.Bold
    )

    LaunchedEffect(key3 = refreshTrigger, key1 = month.intValue, key2 = day.intValue) {
        isTextVisible = false
        scope.launch {
            text = try {
                NumbersComponent().getCurious(month = month.intValue, day = day.intValue)
            } catch (e: Exception) {
                e.localizedMessage ?: "error"
            }
            isTextVisible = true
            isFavorite = false
        }
    }

    Box(
        modifier = Modifier
            .padding(26.dp)
            .fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(25.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.nostradamus),
                            contentDescription = "Nostradamus",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(
                                    CircleShape
                                ),
                            contentScale = ContentScale.Crop
                        )

                        // Favorite Button
                        val iconTintColor by animateColorAsState(
                            targetValue = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onPrimary,
                            label = "Favorite icon color"
                        )

                        IconButton(
                            onClick = {
                                isFavorite = !isFavorite
                                if (isFavorite) {
                                    onEvent(
                                        FactsEvents.SaveFact(
                                            date = "${monthsOfYear[month.intValue - 1].month} ${day.intValue}",
                                            description = text,
                                            month = month.intValue,
                                            day = day.intValue
                                        )
                                    )
                                    // TODO: Check that this works!
                                } else {
                                    val factWithLargestId = state.facts.maxByOrNull { it.id }
                                    if (factWithLargestId != null) {
                                        onEvent(FactsEvents.DeleteFact(factWithLargestId))
                                    }
                                }
                            },
                            modifier = Modifier
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.shapes.small
                                )
                                .padding(3.dp)
                                .size(30.dp),
                        ) {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = "Favorite",
                                tint = iconTintColor
                            )
                        }
                    }
                    val date = "${monthsOfYear[month.intValue - 1].month} ${day.intValue}"

                    Text(
                        text = date,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                    )
                    AnimatedVisibility(
                        visible = isTextVisible,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Text(
                            text = text,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CardButtons(
                            onClick = { openDialog.value = true },
                            icon = Icons.Default.DateRange,
                            contentDescription = "Change Date"
                        )
                        CardButtons(
                            onClick = { refreshTrigger = !refreshTrigger },
                            icon = Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                }
            }
        }
        when {
            openDialog.value ->
                CuriousDatePicker(
                    onDismissRequest = { openDialog.value = false },
                    monthSelected = month,
                    daySelected = day
                )
        }
    }
}

@Composable
fun CardButtons(onClick: () -> Unit, icon: ImageVector, contentDescription: String) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.small)
            .padding(3.dp)
            .size(30.dp),
    ) {
        Icon(
            icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onPrimary,
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuriousDatePicker(
    onDismissRequest: () -> Unit,
    monthSelected: MutableState<Int>,
    daySelected: MutableState<Int>
) {
    var expandedDays by remember { mutableStateOf(false) }
    var expandedMonth by remember { mutableStateOf(false) }

    var selectedMonth by remember { mutableIntStateOf(monthSelected.value - 1) }
    var selectedMonthText by remember { mutableStateOf(monthsOfYear[monthSelected.value - 1].month) }

    var selectedDay by remember { mutableStateOf(daySelected.value.toString()) }

    val days = (1..monthsOfYear[selectedMonth].days).map { it.toString() }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Select Month and Day",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center,
                )

                // Month
                ExposedDropdownMenuBox(
                    expanded = expandedMonth,
                    onExpandedChange = { expandedMonth = !expandedMonth }
                ) {
                    OutlinedTextField(
                        value = selectedMonthText,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Month") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMonth) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedMonth,
                        onDismissRequest = { expandedMonth = false }
                    ) {
                        monthsOfYear.forEach { month ->
                            DropdownMenuItem(
                                text = { Text(month.month) },
                                onClick = {
                                    selectedMonthText = month.month
                                    selectedMonth = month.id

                                    expandedMonth = false

                                    if (selectedDay.toInt() > month.days) {
                                        selectedDay = month.days.toString()
                                    }
                                }
                            )
                        }
                    }
                }

                // Day
                ExposedDropdownMenuBox(
                    expanded = expandedDays,
                    onExpandedChange = { expandedDays = !expandedDays }
                ) {
                    OutlinedTextField(
                        value = selectedDay,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Day") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDays) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedDays,
                        onDismissRequest = { expandedDays = false }
                    ) {
                        days.forEach { day ->
                            DropdownMenuItem(
                                text = { Text(day) },
                                onClick = {
                                    selectedDay = day
                                    expandedDays = false
                                }
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        monthSelected.value = selectedMonth + 1
                        daySelected.value = selectedDay.toInt()

                        onDismissRequest()
                    },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 16.dp)
                ) {
                    Text("Confirm")
                }
            }
        }
    }
}
