package com.example.apptest.feature_train.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.apptest.feature_train.presentation.util.MainScreen
import com.example.apptest.ui.theme.AppTestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable higher refresh rate if available
        setHigherRefreshRate()

        setContent {
            AppTestTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    MainScreen(navController)
                }
            }
        }
    }

    private fun setHigherRefreshRate() {
        val activity = this // or use 'this' directly if inside the Activity
        val window = activity.window
        val params = window.attributes

        // Get the list of supported refresh rates
        val supportedModes = windowManager.defaultDisplay.supportedModes

        // Find the highest supported refresh rate that is either 90Hz or 120Hz
        var preferredRefreshRate = 60f // Default to 60Hz
        for (mode in supportedModes) {
            if (mode.refreshRate >= 119f) { // Check for 120Hz (allow for some float imprecision)
                preferredRefreshRate = mode.refreshRate
                break // Found 120Hz, no need to check further
            } else if (mode.refreshRate >= 89f && preferredRefreshRate < 90f) { // Check for 90Hz
                preferredRefreshRate = mode.refreshRate
            }
        }

        if (preferredRefreshRate > 60f) {
            params.preferredDisplayModeId = supportedModes.find { it.refreshRate >= preferredRefreshRate }?.modeId ?: 0
            window.attributes = params
        }
    }
}