package com.example.apptest.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = BluePrimaryDark,
    onPrimary = Color.White,
    primaryContainer = BluePrimary,
    onPrimaryContainer = Color.White,
    secondary = RedAccentDark,
    onSecondary = Color.White,
    secondaryContainer = RedAccent,
    onSecondaryContainer = Color.White,
    tertiary = Pink80,
    onTertiary = Color.White,
    tertiaryContainer = Pink40,
    onTertiaryContainer = Color.White,
    error = Color(0xFFCF6679),
    onError = Color.White,
    background = GreyBackgroundDark,
    onBackground = Color.White,
    surface = GreyBackgroundDark,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color.White,
    outline = Color(0xFF938F99)
)

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = Color.White,
    primaryContainer = BluePrimaryLight,
    onPrimaryContainer = Color.Black,
    secondary = RedAccent,
    onSecondary = Color.Black,
    secondaryContainer = RedAccentLight,
    onSecondaryContainer = Color.Black,
    tertiary = Pink40,
    onTertiary = Color.Black,
    tertiaryContainer = Pink80,
    onTertiaryContainer = Color.Black,
    error = Color(0xFFB00020),
    onError = Color.White,
    background = GreyBackgroundLight,
    onBackground = Color(0xFF1C1B1F),
    surface = GreyBackgroundLight,
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454F),
    outline = Color(0xFF79747E)
)

@Composable
fun AppTestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    fontScale: Float = 1.0f, // ADDED fontScale parameter with default value
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

    // Create scaled typography
        val scaledTypography = Typography.copy(
        bodyLarge = Typography.bodyLarge.copy(fontSize = Typography.bodyLarge.fontSize * fontScale),
        bodyMedium = Typography.bodyMedium.copy(fontSize = Typography.bodyMedium.fontSize * fontScale),
        bodySmall = Typography.bodySmall.copy(fontSize = Typography.bodySmall.fontSize * fontScale),
        titleLarge = Typography.titleLarge.copy(fontSize = Typography.titleLarge.fontSize * fontScale),
        titleMedium = Typography.titleMedium.copy(fontSize = Typography.titleMedium.fontSize * fontScale),
        titleSmall = Typography.titleSmall.copy(fontSize = Typography.titleSmall.fontSize * fontScale),
        headlineLarge = Typography.headlineLarge.copy(fontSize = Typography.headlineLarge.fontSize * fontScale),
        headlineMedium = Typography.headlineMedium.copy(fontSize = Typography.headlineMedium.fontSize * fontScale),
        headlineSmall = Typography.headlineSmall.copy(fontSize = Typography.headlineSmall.fontSize * fontScale),
        labelLarge = Typography.labelLarge.copy(fontSize = Typography.labelLarge.fontSize * fontScale),
        labelMedium = Typography.labelMedium.copy(fontSize = Typography.labelMedium.fontSize * fontScale),
        labelSmall = Typography.labelSmall.copy(fontSize = Typography.labelSmall.fontSize * fontScale)
        // ... apply scaling to other text styles as needed, add more if you use them
    )


    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = scaledTypography, // Use scaled typography
        content = content
    )
}