package com.croshapss.pushharder.feature_train.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.compose.rememberNavController
import com.croshapss.pushharder.feature_train.presentation.util.MainScreen
import com.croshapss.pushharder.ui.theme.AppTestTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlin.math.roundToInt

private val Context.dataStore by preferencesDataStore("settings")
fun Float.roundToTwoDecimalPlaces(): Float {
    return (this * 100.0f).roundToInt() / 100.0f
}


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Theme Mode Persistence
        val themeModeKey = intPreferencesKey("theme_mode")
        val themeModeFlow = applicationContext.dataStore.data.map { preferences ->
            preferences[themeModeKey] ?: 0 // Default to system theme
        }
        val initialThemeMode = runBlocking { themeModeFlow.first() }

        // Font Scale Persistence
        val fontScaleKey = floatPreferencesKey("font_scale")
        val fontScaleFlow = applicationContext.dataStore.data.map { preferences ->
            preferences[fontScaleKey] ?: 1.0f // Default to 1.0 scale
        }
        val initialFontScale = runBlocking { fontScaleFlow.first() }


        setContent {
            var themeMode by rememberSaveable { mutableIntStateOf(initialThemeMode) }
            var fontScale by rememberSaveable { mutableFloatStateOf(initialFontScale) }

            AppTestTheme(
                darkTheme = when (themeMode) {
                    1 -> false
                    2 -> true
                    else -> isSystemInDarkTheme()
                },
                fontScale = fontScale // Pass fontScale to theme
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    MainScreen(
                        navController = navController,
                        themeMode = themeMode,
                        fontScale = fontScale, // Pass fontScale to MainScreen
                        onThemeModeChange = { newMode ->
                            themeMode = newMode
                            // Save the new theme mode to DataStore
                            runBlocking {
                                applicationContext.dataStore.edit { preferences ->
                                    preferences[themeModeKey] = newMode
                                }
                            }
                        },
                        onFontScaleChange = { newScale -> // Font Scale Change Callback
                            fontScale = newScale
                            runBlocking {
                                applicationContext.dataStore.edit { preferences ->
                                    preferences[fontScaleKey] = newScale
                                }
                            }
                        }
                    )
                }
            }
        }

        // Enable higher refresh rate if available
        setHigherRefreshRate()
    }

    private fun setHigherRefreshRate() {
        val activity = this
        val window = activity.window
        val params = window.attributes

        val supportedModes = windowManager.defaultDisplay.supportedModes

        var preferredRefreshRate = 60f
        for (mode in supportedModes) {
            if (mode.refreshRate >= 119f) {
                preferredRefreshRate = mode.refreshRate
                break
            } else if (mode.refreshRate >= 89f && preferredRefreshRate < 90f) {
                preferredRefreshRate = mode.refreshRate
            }
        }

        if (preferredRefreshRate > 60f) {
            params.preferredDisplayModeId = supportedModes.find { it.refreshRate >= preferredRefreshRate }?.modeId ?: 0
            window.attributes = params
        }
    }
}