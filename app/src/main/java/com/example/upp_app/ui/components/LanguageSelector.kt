package com.example.upp_app.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.upp_app.R
import com.example.upp_app.ui.theme.NeutralAccent
import com.example.upp_app.ui.theme.NeutralPrimary
import com.example.upp_app.ui.theme.ThemeManager

data class Language(
    val code: String,
    val name: String,
    val flag: Int
)

@Composable
fun LanguageSelector(
    onDismiss: () -> Unit,
    onLanguageSelected: (String) -> Unit
) {
    val languages = listOf(
        Language("en", "English", R.drawable.ic_flag_us),
        Language("es", "Español", R.drawable.ic_flag_es)
    )

    var selectedLanguage by remember { mutableStateOf(ThemeManager.currentLanguage) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (ThemeManager.currentLanguage == "es") "Seleccionar idioma" else "Select Language",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column {
                languages.forEach { language ->
                    LanguageItem(
                        language = language,
                        isSelected = selectedLanguage == language.code,
                        onClick = { selectedLanguage = language.code }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onLanguageSelected(selectedLanguage)
                    onDismiss()
                }
            ) {
                Text(if (ThemeManager.currentLanguage == "es") "Confirmar" else "Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (ThemeManager.currentLanguage == "es") "Cancelar" else "Cancel")
            }
        }
    )
}

@Composable
fun LanguageItem(
    language: Language,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .background(
                if (isSelected) NeutralPrimary.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = language.flag),
            contentDescription = language.name,
            modifier = Modifier.size(24.dp),
            tint = NeutralAccent
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = language.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = NeutralPrimary
            )
        )
    }
}

