package com.example.upp_app.ui.screen.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.upp_app.R
import com.example.upp_app.api.AuthManager
import com.example.upp_app.ui.components.PrimaryButton
import com.example.upp_app.ui.theme.*
import com.example.upp_app.ui.utils.localizedString
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val authManager = remember { AuthManager(context) }

    // Estados de usuario
    var userName by remember { mutableStateOf(authManager.getUserName() ?: "") }
    var userEmail by remember { mutableStateOf(authManager.getUserEmail() ?: "") }
    var userPhone by remember { mutableStateOf("+1 (555) 123-4567") }
    var userLocation by remember { mutableStateOf("San Francisco, CA") }

    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

    var showEditDialog by remember { mutableStateOf(false) }
    var editField by remember { mutableStateOf("") }
    var editValue by remember { mutableStateOf("") }
    var editTitle by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    // Refrescar datos al entrar en composición
    LaunchedEffect(Unit) {
        userName = authManager.getUserName() ?: ""
        userEmail = authManager.getUserEmail() ?: ""
        // carga otros datos si es necesario
    }

    // Textos localizados
    val profileText = localizedString("profile")
    val userText = localizedString("user")
    val editProfileText = localizedString("edit_profile")
    val personalInfoText = localizedString("personal_information")
    val phoneText = localizedString("phone")
    val locationText = localizedString("location")
    val joinedText = localizedString("joined")
    val preferencesText = localizedString("preferences")
    val languageText = localizedString("language")
    val themeText = localizedString("theme")
    val notificationsText = localizedString("notifications")
    val signOutText = localizedString("sign_out")
    val saveText = localizedString("save")
    val cancelText = localizedString("cancel")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = profileText,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        editField = "name"
                        editValue = userName
                        editTitle = editProfileText
                        showEditDialog = true
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_edit),
                            contentDescription = "Edit Profile",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mensajes
            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = ErrorColor, modifier = Modifier.padding(16.dp))
            }
            if (successMessage.isNotEmpty()) {
                Text(successMessage, color = SuccessColor, modifier = Modifier.padding(16.dp))
            }

            // Sección perfil
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 1.dp
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                            .border(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), CircleShape)
                            .clickable { /* cambiar foto */ },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_person),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(60.dp)
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = userName.ifEmpty { userText },
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = userEmail,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    Spacer(Modifier.height(24.dp))

                    // Estadísticas...
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem("28", "Chats", InfoColor)
                        StatItem("5", "Days", SuccessColor)
                        StatItem("Pro", "Plan", MaterialTheme.colorScheme.primary)
                    }
                }
            }

            // Secciones de información personal y preferencias...
            // Sección de información personal
            ProfileSection(
                title = personalInfoText,
                items = listOf(
                    ProfileItem(
                        icon = R.drawable.ic_phone,
                        title = phoneText,
                        value = userPhone,
                        onEdit = {
                            editField = "phone"
                            editValue = userPhone
                            editTitle = "Edit Phone"
                            showEditDialog = true
                        }
                    ),
                    ProfileItem(
                        icon = R.drawable.ic_location,
                        title = locationText,
                        value = userLocation,
                        onEdit = {
                            editField = "location"
                            editValue = userLocation
                            editTitle = "Edit Location"
                            showEditDialog = true
                        }
                    ),
                    ProfileItem(
                        icon = R.drawable.ic_calendar,
                        title = joinedText,
                        value = "March 2023"
                    )
                )
            )

            // Sección de preferencias
            ProfileSection(
                title = preferencesText,
                items = listOf(
                    ProfileItem(
                        icon = R.drawable.ic_language,
                        title = languageText,
                        value = if (ThemeManager.currentLanguage == "es") "Español" else "English",
                        onEdit = {
                            // Aquí se podría implementar la selección de idioma
                        }
                    ),
                    ProfileItem(
                        icon = R.drawable.ic_theme,
                        title = themeText,
                        value = if (ThemeManager.isDarkTheme) "Dark" else "Light",
                        onEdit = {
                            // Aquí se podría implementar la selección de tema
                        }
                    ),
                    ProfileItem(
                        icon = R.drawable.ic_notifications,
                        title = notificationsText,
                        value = "Enabled",
                        onEdit = {
                            // Aquí se podría implementar la configuración de notificaciones
                        }
                    )
                )
            )

            Spacer(Modifier.height(24.dp))

            // Botón de cerrar sesión
            PrimaryButton(
                onClick = {
                    authManager.logout()
                    navController.navigate("login") {
                        popUpTo("profile") { inclusive = true }
                    }
                },
                text = signOutText,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(24.dp))
        }

        // Diálogo de edición
        if (showEditDialog) {
            EditDialog(
                title = editTitle,
                value = editValue,
                saveText = saveText,
                cancelText = cancelText,
                onDismiss = { showEditDialog = false },
                onConfirm = { newValue ->
                    showEditDialog = false
                    isLoading = true
                    coroutineScope.launch {
                        try {
                            when (editField) {
                                "name" -> {
                                    // Actualizar nombre via AuthManager
                                    //authManager.updateUserProfile(newValue)
                                    userName = newValue
                                    successMessage = "Name updated successfully"
                                }
                                "phone" -> {
                                    userPhone = newValue
                                    successMessage = "Phone updated successfully"
                                }
                                "location" -> {
                                    userLocation = newValue
                                    successMessage = "Location updated successfully"
                                }
                            }
                        } catch (e: Exception) {
                            errorMessage = e.message ?: "Failed to update profile"
                        } finally {
                            isLoading = false
                        }
                    }
                }
            )
        }

        // Indicador de carga
        if (isLoading) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}


@Composable
fun StatItem(
    value: String,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = color
        )

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

data class ProfileItem(
    val icon: Int,
    val title: String,
    val value: String,
    val onEdit: (() -> Unit)? = null
)

@Composable
fun ProfileSection(
    title: String,
    items: List<ProfileItem>
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
                    ProfileItemRow(item = item)

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

@Composable
fun ProfileItemRow(
    item: ProfileItem
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = item.icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Text(
                text = item.value,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (item.onEdit != null) {
            IconButton(
                onClick = { item.onEdit.invoke() },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "Edit",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.size(16.dp)
                )
            }
        } else {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDialog(
    title: String,
    value: String,
    saveText: String,
    cancelText: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var editedValue by remember { mutableStateOf(value) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = title)
        },
        text = {
            OutlinedTextField(
                value = editedValue,
                onValueChange = { editedValue = it },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(editedValue) }
            ) {
                Text(saveText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(cancelText)
            }
        }
    )
}

