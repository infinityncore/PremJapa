package com.premjapa.radhanamecounter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.premjapa.radhanamecounter.ui.CounterScreen
import com.premjapa.radhanamecounter.ui.theme.RadhaNameCounterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            RadhaNameCounterTheme {
                val vm: RadhaCounterViewModel = viewModel(
                    factory = RadhaCounterViewModel.factory(applicationContext)
                )
                val state by vm.uiState.collectAsStateWithLifecycle()

                CounterScreen(
                    state = state,
                    onIncrement = vm::increment,
                    onDecrement = vm::decrement,
                    onSetDailyGoal = vm::setDailyGoal,
                    onResetDay = vm::resetToday,
                    onResetAll = vm::resetAll
                )
            }
        }
    }
}
