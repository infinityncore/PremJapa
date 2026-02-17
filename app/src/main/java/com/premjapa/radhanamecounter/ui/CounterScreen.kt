package com.premjapa.radhanamecounter.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.premjapa.radhanamecounter.CounterUiState
import kotlin.math.roundToInt

@Composable
fun CounterScreen(
    state: CounterUiState,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onSetDailyGoal: (Int) -> Unit,
    onResetDay: () -> Unit,
    onResetAll: () -> Unit
) {
    val gradient = Brush.verticalGradient(listOf(Color(0xFFFFF7EB), Color(0xFFFFE2C6)))
    var goalSlider by remember(state.dailyGoal) { mutableFloatStateOf(state.dailyGoal.toFloat()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(horizontal = 16.dp, vertical = 24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Radha Naam Japa",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6A2D16)
        )

        Card(colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.88f))) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Today's Chanting", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "${state.todayCount}",
                    style = MaterialTheme.typography.displayLarge,
                    color = Color(0xFFC65824),
                    fontWeight = FontWeight.Bold
                )
                LinearProgressIndicator(
                    progress = { state.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(50)),
                    color = Color(0xFFC65824),
                    trackColor = Color(0xFFFFD5B8)
                )
                Text("Goal: ${state.dailyGoal} names", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = onDecrement, modifier = Modifier.weight(1f)) { Text("-1") }
            Button(onClick = onIncrement, modifier = Modifier.weight(2f)) { Text("Chant +1") }
        }

        Card(colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Daily Sankalpa", style = MaterialTheme.typography.titleMedium)
                Text("Set your target: ${goalSlider.roundToInt()} names")
                Slider(
                    value = goalSlider,
                    onValueChange = { goalSlider = it },
                    valueRange = 9f..1008f
                )
                Button(onClick = { onSetDailyGoal(goalSlider.roundToInt()) }) {
                    Text("Save Goal")
                }
            }
        }

        Card(colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatItem("Total", state.totalCount.toString())
                StatItem("Current Streak", "${state.currentStreakDays}d")
                StatItem("Best", "${state.longestStreakDays}d")
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onResetDay, modifier = Modifier.weight(1f)) {
                Text("Reset Today")
            }
            OutlinedButton(onClick = onResetAll, modifier = Modifier.weight(1f)) {
                Text("Reset All")
            }
        }
    }
}

@Composable
private fun StatItem(title: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(text = title, style = MaterialTheme.typography.bodySmall)
    }
}
