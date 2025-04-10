package com.example.upp_app.ui.screen.settings


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.upp_app.R
import com.example.upp_app.ui.components.LanguageSelector
import com.example.upp_app.ui.theme.Background
import com.example.upp_app.ui.theme.BorderColor
import com.example.upp_app.ui.theme.ErrorColor
import com.example.upp_app.ui.theme.InfoColor
import com.example.upp_app.ui.theme.LightTextColor
import com.example.upp_app.ui.theme.MediumTextColor
import com.example.upp_app.ui.theme.NeutralAccent
import com.example.upp_app.ui.theme.NeutralPrimary
import com.example.upp_app.ui.theme.SuccessColor
import com.example.upp_app.ui.theme.ThemeManager
import com.example.upp_app.ui.theme.WarningColor
import com.example.upp_app.ui.utils.LocalizationUtils
import com.example.upp_app.ui.utils.localizedString
import com.example.upp_app.api.AuthManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var isLoading by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var confirmDialogTitle by remember { mutableStateOf("") }
    var confirmDialogMessage by remember { mutableStateOf("") }
    var confirmDialogAction by remember { mutableStateOf({}) }

    // Mostrar selector de idioma
    var showLanguageSelector by remember { mutableStateOf(false) }

    // Preferencias
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(ThemeManager.isDarkTheme) }
    var privacyModeEnabled by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    // Sustituye FirebaseAuth por AuthManager:
    val authManager = remember { AuthManager(context) }

    // Get localized strings outside of lambdas
    val settingsText = localizedString("settings")
    val accountText = localizedString("account")
    val profileText = localizedString("profile")
    val securityText = localizedString("security")
    val subscriptionText = localizedString("subscription")
    val preferencesText = localizedString("preferences")
    val notificationsText = localizedString("notifications")
    val darkModeText = localizedString("dark_mode")
    val languageText = localizedString("language")
    val dataPrivacyText = localizedString("data_privacy")
    val dataUsageText = localizedString("data_usage")
    val privacyModeText = localizedString("privacy_mode")
    val clearDataText = localizedString("clear_data")
    val helpSupportText = localizedString("help_support")
    val helpCenterText = localizedString("help_center")
    val sendFeedbackText = localizedString("send_feedback")
    val aboutText = localizedString("about")
    val confirmText = localizedString("confirm")
    val cancelText = localizedString("cancel")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = settingsText,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        // Modificar el color de fondo principal para adaptarse al tema
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Sección de cuenta
            SettingsSection(
                title = accountText,
                items = listOf(
                    SettingItem(
                        icon = R.drawable.ic_person,
                        title = profileText,
                        description = "Manage your personal information",
                        iconTint = InfoColor,
                        onClick = {
                            navController.navigate("profile")
                        }
                    ),
                    SettingItem(
                        icon = R.drawable.ic_security,
                        title = securityText,
                        description = "Password and authentication",
                        iconTint = SuccessColor,
                        onClick = {
                            confirmDialogTitle = "Change Password"
                            confirmDialogMessage = "We'll send a password reset link to your email address."
                            confirmDialogAction = {
                                isLoading = true
                                coroutineScope.launch {
                                    isLoading = false
                                    try {
                                        // Usa AuthManager en lugar de FirebaseAuth
                                        val email = authManager.getUserEmail() ?: ""
                                        val result = authManager.sendPasswordResetEmail(email)
                                    } catch (e: Exception) {
                                        isLoading = false
                                    }
                                }
                            }
                            showConfirmDialog = true
                        }
                    ),
                    SettingItem(
                        icon = R.drawable.ic_payment,
                        title = subscriptionText,
                        description = "Manage your subscription plan",
                        iconTint = WarningColor,
                        onClick = {
                            // Aquí se podría implementar la navegación a la pantalla de suscripción
                        }
                    )
                )
            )

            // Sección de preferencias
            SettingsSection(
                title = preferencesText,
                items = listOf(
                    SettingToggleItem(
                        icon = R.drawable.ic_notifications,
                        title = notificationsText,
                        description = "Receive alerts and reminders",
                        iconTint = NeutralPrimary,
                        isChecked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    ),
                    SettingToggleItem(
                        icon = R.drawable.ic_theme,
                        title = darkModeText,
                        description = "Switch between light and dark themes",
                        iconTint = NeutralAccent,
                        isChecked = darkModeEnabled,
                        onCheckedChange = {
                            darkModeEnabled = it
                            ThemeManager.toggleDarkTheme(it)
                        }
                    ),
                    SettingItem(
                        icon = R.drawable.ic_language,
                        title = languageText,
                        description = "Change application language",
                        iconTint = InfoColor,
                        value = if (ThemeManager.currentLanguage == "es") "Español" else "English",
                        onClick = {
                            showLanguageSelector = true
                        }
                    )
                )
            )

            // Sección de datos
            SettingsSection(
                title = dataPrivacyText,
                items = listOf(
                    SettingItem(
                        icon = R.drawable.ic_data,
                        title = dataUsageText,
                        description = "Manage how the app uses data",
                        iconTint = InfoColor,
                        onClick = {
                            // Aquí se podría implementar la navegación a la pantalla de uso de datos
                        }
                    ),
                    SettingToggleItem(
                        icon = R.drawable.ic_privacy,
                        title = privacyModeText,
                        description = "Enhanced privacy protection",
                        iconTint = SuccessColor,
                        isChecked = privacyModeEnabled,
                        onCheckedChange = { privacyModeEnabled = it }
                    ),
                    SettingItem(
                        icon = R.drawable.ic_delete,
                        title = clearDataText,
                        description = "Delete all app data and history",
                        iconTint = ErrorColor,
                        onClick = {
                            confirmDialogTitle = clearDataText
                            confirmDialogMessage = "This will delete all your app data and history. This action cannot be undone."
                            confirmDialogAction = {
                                // Aquí se implementaría la limpieza de datos
                                isLoading = true
                                // Simular proceso
                                coroutineScope.launch {
                                    kotlinx.coroutines.delay(1500)
                                    isLoading = false
                                }
                            }
                            showConfirmDialog = true
                        }
                    )
                )
            )

            // Sección de ayuda
            SettingsSection(
                title = helpSupportText,
                items = listOf(
                    SettingItem(
                        icon = R.drawable.ic_help,
                        title = helpCenterText,
                        description = "Get help with using the app",
                        iconTint = InfoColor,
                        onClick = {
                            // Aquí se podría implementar la navegación a la pantalla de ayuda
                        }
                    ),
                    SettingItem(
                        icon = R.drawable.ic_feedback,
                        title = sendFeedbackText,
                        description = "Help us improve the app",
                        iconTint = WarningColor,
                        onClick = {
                            // Aquí se podría implementar el envío de feedback
                        }
                    ),
                    SettingItem(
                        icon = R.drawable.ic_info,
                        title = aboutText,
                        description = "App version and information",
                        iconTint = NeutralPrimary,
                        value = "v1.0.0",
                        onClick = {
                            // Aquí se podría implementar la navegación a la pantalla de información
                        }
                    )
                )
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Diálogo de confirmación
        if (showConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmDialog = false },
                title = {
                    Text(text = confirmDialogTitle)
                },
                text = {
                    Text(text = confirmDialogMessage)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showConfirmDialog = false
                            confirmDialogAction()
                        }
                    ) {
                        Text(confirmText)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showConfirmDialog = false }
                    ) {
                        Text(cancelText)
                    }
                }
            )
        }

        // Selector de idioma
        if (showLanguageSelector) {
            LanguageSelector(
                onDismiss = { showLanguageSelector = false },
                onLanguageSelected = { languageCode ->
                    ThemeManager.setLanguage(languageCode)
                    // En una aplicación real, aquí se cambiaría la configuración de idioma del sistema
                    // y se reiniciaría la actividad para aplicar los cambios
                }
            )
        }

        // Indicador de carga
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

data class SettingItem(
    val icon: Int,
    val title: String,
    val description: String,
    val iconTint: Color,
    val value: String? = null,
    val onClick: () -> Unit = {}
)

data class SettingToggleItem(
    val icon: Int,
    val title: String,
    val description: String,
    val iconTint: Color,
    val isChecked: Boolean,
    val onCheckedChange: (Boolean) -> Unit
)

// Modificar los colores de fondo y texto en las secciones para adaptarse al tema
@Composable
fun SettingsSection(
    title: String,
    items: List<Any>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 1.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                items.forEachIndexed { index, item ->
                    when (item) {
                        is SettingItem -> SettingItemRow(item = item)
                        is SettingToggleItem -> SettingToggleRow(item = item)
                    }

                    if (index < items.size - 1) {
                        Divider(
                            color = if (ThemeManager.isDarkTheme) Color.DarkGray else BorderColor,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

// Modificar los colores de texto en las filas de configuración
@Composable
fun SettingItemRow(
    item: SettingItem
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(item.iconTint.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = item.icon),
                contentDescription = null,
                tint = item.iconTint,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = item.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        if (item.value != null) {
            Text(
                text = item.value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            modifier = Modifier.size(16.dp)
        )
    }
}

// Modificar los colores de texto en las filas de configuración con interruptor
@Composable
fun SettingToggleRow(
    item: SettingToggleItem
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(item.iconTint.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = item.icon),
                contentDescription = null,
                tint = item.iconTint,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = item.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        Switch(
            checked = item.isChecked,
            onCheckedChange = item.onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = if (ThemeManager.isDarkTheme) Color.DarkGray else LightTextColor
            )
        )
    }
}

