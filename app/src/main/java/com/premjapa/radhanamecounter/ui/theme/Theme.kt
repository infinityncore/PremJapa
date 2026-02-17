package com.premjapa.radhanamecounter.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFFC65824),
    secondary = Color(0xFF8F3B17),
    tertiary = Color(0xFFE59A3F),
    surface = Color(0xFFFFFAF4)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFFB68A),
    secondary = Color(0xFFFFCF9D),
    tertiary = Color(0xFFF8C573)
)

@Composable
fun RadhaNameCounterTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}
