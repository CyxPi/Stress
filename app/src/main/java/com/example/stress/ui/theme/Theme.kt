package com.example.stress.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Calm, soft color scheme - avoids dynamic colors for consistent calming experience
private val LightColorScheme = lightColorScheme(
    primary = SageGreen,
    onPrimary = Color.White,
    primaryContainer = SageGreenLight,
    onPrimaryContainer = WarmGray600,
    secondary = Lavender,
    onSecondary = Color.White,
    secondaryContainer = LavenderLight,
    onSecondaryContainer = WarmGray600,
    tertiary = SoftCoral,
    onTertiary = WarmGray600,
    background = WarmGray100,
    onBackground = WarmGray600,
    surface = Color.White,
    onSurface = WarmGray600,
    surfaceVariant = WarmGray200,
    onSurfaceVariant = WarmGray500,
    outline = WarmGray300,
    error = Color(0xFFBA1A1A),
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = SageGreen80,
    onPrimary = WarmGrayDark,
    primaryContainer = SageGreenDark,
    onPrimaryContainer = SageGreenLight,
    secondary = Lavender80,
    onSecondary = WarmGrayDark,
    secondaryContainer = LavenderDark,
    onSecondaryContainer = LavenderLight,
    tertiary = SoftCoralLight,
    onTertiary = WarmGrayDark,
    background = WarmGrayDark,
    onBackground = WarmGray200,
    surface = WarmGrayDarkSurface,
    onSurface = WarmGray200,
    surfaceVariant = WarmGray500,
    onSurfaceVariant = WarmGray300,
    outline = WarmGray400,
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

@Composable
fun StressTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Disabled for consistent calm palette
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

    val view = LocalView.current
    if (!view.isInEditMode && view.context is Activity) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
