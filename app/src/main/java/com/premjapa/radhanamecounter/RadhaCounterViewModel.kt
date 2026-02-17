package com.premjapa.radhanamecounter

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import kotlin.math.max

data class CounterUiState(
    val totalCount: Int = 0,
    val todayCount: Int = 0,
    val dailyGoal: Int = 108,
    val longestStreakDays: Int = 0,
    val currentStreakDays: Int = 0,
    val lastUpdatedDate: String = LocalDate.now().toString()
) {
    val progress: Float = (todayCount.toFloat() / dailyGoal.coerceAtLeast(1)).coerceIn(0f, 1f)
}

class RadhaCounterViewModel(private val preferences: SharedPreferences) : ViewModel() {
    private val _uiState = MutableStateFlow(loadState())
    val uiState: StateFlow<CounterUiState> = _uiState.asStateFlow()

    fun increment() {
        syncDateRollOver()
        updateState {
            val nextToday = it.todayCount + 1
            val nextGoalMet = nextToday >= it.dailyGoal
            val streakBase = it.currentStreakDays.coerceAtLeast(1)

            it.copy(
                totalCount = it.totalCount + 1,
                todayCount = nextToday,
                currentStreakDays = if (nextGoalMet) max(streakBase, it.currentStreakDays) else it.currentStreakDays,
                longestStreakDays = if (nextGoalMet) max(it.longestStreakDays, streakBase) else it.longestStreakDays
            )
        }
    }

    fun decrement() {
        syncDateRollOver()
        updateState {
            if (it.todayCount == 0 || it.totalCount == 0) it
            else it.copy(totalCount = it.totalCount - 1, todayCount = it.todayCount - 1)
        }
    }

    fun setDailyGoal(goal: Int) {
        val safeGoal = goal.coerceIn(9, 1008)
        updateState { it.copy(dailyGoal = safeGoal) }
    }

    fun resetToday() {
        syncDateRollOver()
        updateState { it.copy(todayCount = 0) }
    }

    fun resetAll() {
        updateState { CounterUiState(lastUpdatedDate = LocalDate.now().toString()) }
    }

    private fun syncDateRollOver() {
        val today = LocalDate.now().toString()
        val current = _uiState.value
        if (current.lastUpdatedDate == today) return

        val goalHitYesterday = current.todayCount >= current.dailyGoal
        val nextStreak = if (goalHitYesterday) current.currentStreakDays + 1 else 0

        updateState {
            it.copy(
                todayCount = 0,
                currentStreakDays = nextStreak,
                longestStreakDays = max(it.longestStreakDays, nextStreak),
                lastUpdatedDate = today
            )
        }
    }

    private fun updateState(transform: (CounterUiState) -> CounterUiState) {
        val updated = transform(_uiState.value)
        _uiState.value = updated
        saveState(updated)
    }

    private fun loadState(): CounterUiState {
        return CounterUiState(
            totalCount = preferences.getInt(KEY_TOTAL, 0),
            todayCount = preferences.getInt(KEY_TODAY, 0),
            dailyGoal = preferences.getInt(KEY_GOAL, 108),
            longestStreakDays = preferences.getInt(KEY_LONGEST_STREAK, 0),
            currentStreakDays = preferences.getInt(KEY_CURRENT_STREAK, 0),
            lastUpdatedDate = preferences.getString(KEY_LAST_DATE, LocalDate.now().toString()) ?: LocalDate.now().toString()
        )
    }

    private fun saveState(state: CounterUiState) {
        preferences.edit()
            .putInt(KEY_TOTAL, state.totalCount)
            .putInt(KEY_TODAY, state.todayCount)
            .putInt(KEY_GOAL, state.dailyGoal)
            .putInt(KEY_LONGEST_STREAK, state.longestStreakDays)
            .putInt(KEY_CURRENT_STREAK, state.currentStreakDays)
            .putString(KEY_LAST_DATE, state.lastUpdatedDate)
            .apply()
    }

    companion object {
        private const val PREF_NAME = "radha_counter"
        private const val KEY_TOTAL = "total_count"
        private const val KEY_TODAY = "today_count"
        private const val KEY_GOAL = "daily_goal"
        private const val KEY_LONGEST_STREAK = "longest_streak"
        private const val KEY_CURRENT_STREAK = "current_streak"
        private const val KEY_LAST_DATE = "last_date"

        fun factory(context: Context): ViewModelProvider.Factory {
            val appContext = context.applicationContext
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    val prefs = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                    return RadhaCounterViewModel(prefs) as T
                }
            }
        }
    }
}
