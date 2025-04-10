package com.example.upp_app.ui.theme


import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.intl.LocaleList

// Singleton para gestionar el tema y el idioma de la aplicación
object ThemeManager {
    // Estado para el modo oscuro
    var isDarkTheme by mutableStateOf(false)
        private set

    // Estado para el idioma actual
    var currentLanguage by mutableStateOf("en")
        private set

    // Función para cambiar el modo oscuro
    fun toggleDarkTheme(isDark: Boolean) {
        isDarkTheme = isDark
    }

    // Función para cambiar el idioma
    fun setLanguage(languageCode: String) {
        currentLanguage = languageCode
    }

    // Obtener el idioma actual como Locale
    fun getCurrentLocale(): Locale {
        return when (currentLanguage) {
            "es" -> Locale("es")
            else -> Locale("en")
        }
    }
}

// CompositionLocal para acceder al ThemeManager desde cualquier parte de la composición
val LocalThemeManager = compositionLocalOf { ThemeManager }

