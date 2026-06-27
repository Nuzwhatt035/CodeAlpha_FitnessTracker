package com.example.fitnesstrackerapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Deep Charcoal & Emerald Dark Palette
val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF81C784), // Bright Mint Green
    onPrimary = Color(0xFF121212),
    primaryContainer = Color(0xFF1B5E20),
    surface = Color(0xFF1E1E1E), // Deep Charcoal
    onSurface = Color(0xFFE0E0E0),
    background = Color(0xFF121212),
    onBackground = Color(0xFFE0E0E0),
    outline = Color(0xFF424242)
)
/* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
// Premium Soft Cream Light Palette (Neumorphic Base)
val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2E7D32), // Soft Muted Green
    onPrimary = Color.White,
    primaryContainer = Color(0xFFC8E6C9), // Pastel Green Tint
    onPrimaryContainer = Color(0xFF1B5E20),
    surface = Color(0xFFFAF8F5), // Warm Cream / Off-white (Neumorphic background)
    onSurface = Color(0xFF2C2A29),
    background = Color(0xFFFAF8F5),
    onBackground = Color(0xFF2C2A29),
    outline = Color(0xFFD7CCC8), // Soft clay border
    errorContainer = Color(0xFFFFCDD2),
    onErrorContainer = Color(0xFFB71C1C)
)


@Composable
fun FitnessTrackerAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}