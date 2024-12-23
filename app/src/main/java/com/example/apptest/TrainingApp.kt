package com.example.apptest

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Don't forget tot specify this file ass appname in the manifest file!!!
// android:name=".TrainingApp"
@HiltAndroidApp
class TrainingApp : Application() {
}