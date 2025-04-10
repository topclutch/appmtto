package com.example.upp_app.ui.utils

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext

val LocalActivity = staticCompositionLocalOf<Activity> {
    error("No activity found!")
}

@Composable
fun ProvideActivity(activity: Activity, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalActivity provides activity) {
        content()
    }
}
