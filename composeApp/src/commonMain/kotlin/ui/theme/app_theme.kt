package ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

enum class AppTheme {
    Light,
    Dark,
    System
}

private val lightScheme = lightColorScheme(
    primary = Color(10, 99, 0),
    onPrimary = Color.White,
    secondary = Color(99, 40, 0), // Brown
    onSecondary = Color(255, 255, 255), // White (RGB: 255, 255, 255)
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.LightGray,
    onSurface = Color(0, 0, 0), // Black (RGB: 0, 0, 0)
    error = Color.Red,
    onError = Color.White
)

private val darkScheme = darkColorScheme(
    primary = Color(99, 40, 0), // Brown
    onPrimary = Color(255, 255, 255), // White (RGB: 255, 255, 255)
    secondary = Color(10, 99, 0), // Green
    onSecondary = Color.White,
    background = Color(0, 0, 0), // Black (RGB: 0, 0, 0)
    onBackground = Color(255, 255, 255), // White (RGB: 255, 255, 255)
    surface = Color(26, 26, 26), // Dark Gray (RGB: 26, 26, 26)
    onSurface = Color(255, 255, 255), // White (RGB: 255, 255, 255)
    error = Color(255, 176, 32), // Red (RGB: 255, 176, 32)
    onError = Color(0, 0, 0) // Black (RGB: 0, 0, 0)
)



@Composable
fun AppTheme(
    mode: AppTheme = AppTheme.System,
    content: @Composable () -> Unit,
) {
    val isDark = isSystemInDarkTheme()
    val colorScheme = remember(mode) {
        when (mode) {
            AppTheme.Light -> lightScheme
            AppTheme.Dark -> darkScheme
            AppTheme.System -> if (isDark) darkScheme else lightScheme
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}