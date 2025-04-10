package com.example.upp_app.ui.screen.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.upp_app.R
import com.example.upp_app.api.AuthManager
import com.example.upp_app.ui.components.InputField
import com.example.upp_app.ui.components.Link
import com.example.upp_app.ui.components.PrimaryButton
import com.example.upp_app.ui.components.SocialButton
import com.example.upp_app.ui.theme.Background
import com.example.upp_app.ui.theme.ErrorColor
import com.example.upp_app.ui.theme.MediumTextColor
import com.example.upp_app.ui.theme.NeutralPrimary
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var termsAccepted by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var enableBiometric by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val authManager = remember { AuthManager(context) }
    val coroutineScope = rememberCoroutineScope()

    // Check if biometric is available
    val isBiometricAvailable = authManager.isBiometricAvailable()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Fill your information below or register with your social account.",
            style = MaterialTheme.typography.bodyMedium,
            color = MediumTextColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Campo de nombre
        InputField(
            placeholder = "Name",
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_person),
                    contentDescription = null,
                    tint = MediumTextColor,
                    modifier = Modifier.size(20.dp)
                )
            },
            value = name,
            onValueChange = { name = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de email
        InputField(
            placeholder = "Email",
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_email),
                    contentDescription = null,
                    tint = MediumTextColor,
                    modifier = Modifier.size(20.dp)
                )
            },
            value = email,
            onValueChange = { email = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de contraseña
        InputField(
            placeholder = "Password",
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_lock),
                    contentDescription = null,
                    tint = MediumTextColor,
                    modifier = Modifier.size(20.dp)
                )
            },
            isPassword = true,
            passwordVisible = passwordVisible,
            onPasswordVisibilityToggle = { passwordVisible = !passwordVisible },
            value = password,
            onValueChange = { password = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de confirmar contraseña
        InputField(
            placeholder = "Confirm Password",
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_lock),
                    contentDescription = null,
                    tint = MediumTextColor,
                    modifier = Modifier.size(20.dp)
                )
            },
            isPassword = true,
            passwordVisible = confirmPasswordVisible,
            onPasswordVisibilityToggle = { confirmPasswordVisible = !confirmPasswordVisible },
            value = confirmPassword,
            onValueChange = { confirmPassword = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Checkbox para términos y condiciones
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Checkbox(
                checked = termsAccepted,
                onCheckedChange = { termsAccepted = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = NeutralPrimary,
                    uncheckedColor = MediumTextColor
                )
            )
            Text(
                text = "Agree with ",
                color = MediumTextColor
            )
            Link(text = "Terms & Condition") { /* Mostrar términos y condiciones */ }
        }

        // Opción de biometría si está disponible
        if (isBiometricAvailable) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Checkbox(
                    checked = enableBiometric,
                    onCheckedChange = { enableBiometric = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = NeutralPrimary,
                        uncheckedColor = MediumTextColor
                    )
                )
                Text(
                    text = "Enable fingerprint login",
                    color = MediumTextColor
                )
            }
        }

        // Mostrar mensaje de error si existe
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = ErrorColor,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de registro con Laravel API
        PrimaryButton(
            onClick = {
                // Validar campos antes de intentar registrar
                errorMessage = ""

                when {
                    name.isEmpty() -> {
                        errorMessage = "Please enter your name"
                    }
                    email.isEmpty() -> {
                        errorMessage = "Please enter your email"
                    }
                    !isValidEmail(email) -> {
                        errorMessage = "Please enter a valid email address"
                    }
                    password.isEmpty() -> {
                        errorMessage = "Please enter a password"
                    }
                    password.length < 6 -> {
                        errorMessage = "Password must be at least 6 characters"
                    }
                    password != confirmPassword -> {
                        errorMessage = "Passwords do not match"
                    }
                    !termsAccepted -> {
                        errorMessage = "You must accept the terms and conditions"
                    }
                    else -> {
                        isLoading = true
                        // Se usa una corrutina para manejar el registro
                        coroutineScope.launch {
                            try {
                                val result = authManager.register(name, email, password)
                                isLoading = false

                                if (result.isSuccess) {
                                    // Si se habilitó la biometría, guardar la configuración
                                    if (enableBiometric) {
                                        authManager.enableBiometricLogin(true)
                                    }

                                    // Registro exitoso, navegar a la pantalla principal
                                    navController.navigate("home") {
                                        popUpTo("register") { inclusive = true }
                                    }
                                } else {
                                    // Manejar errores específicos
                                    errorMessage = when {
                                        result.exceptionOrNull()?.message?.contains("email address is already in use") == true ->
                                            "This email is already registered"
                                        result.exceptionOrNull()?.message?.contains("network error") == true ->
                                            "Network error. Please check your connection"
                                        else -> result.exceptionOrNull()?.message ?: "Registration failed"
                                    }
                                }
                            } catch (e: Exception) {
                                isLoading = false
                                errorMessage = e.message ?: "An unexpected error occurred"
                            }
                        }
                    }
                }
            },
            text = if (isLoading) "Creating Account..." else "Sign Up",
            modifier = Modifier.padding(horizontal = 16.dp),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Or sign up with",
            style = MaterialTheme.typography.bodyMedium,
            color = MediumTextColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Opciones de registro con redes sociales
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SocialButton(
                iconResId = R.drawable.ic_apple,
                contentDescription = "Sign in with Apple",
                onClick = { /* Implementar registro con Apple */ }
            )

            Spacer(modifier = Modifier.width(24.dp))

            SocialButton(
                iconResId = R.drawable.ic_google,
                contentDescription = "Sign in with Google",
                onClick = { /* Implementar registro con Google */ }
            )

            Spacer(modifier = Modifier.width(24.dp))

            SocialButton(
                iconResId = R.drawable.ic_facebook,
                contentDescription = "Sign in with Facebook",
                onClick = { /* Implementar registro con Facebook */ }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Enlace para iniciar sesión
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Text(
                text = "Already have an account?",
                color = MediumTextColor
            )
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Link(text = "Sign In") {
                navController.navigate("login") {
                    popUpTo("register") { inclusive = true }
                }
            }
        }
    }
}

// Función para validar formato de email
private fun isValidEmail(email: String): Boolean {
    val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
    return email.matches(emailRegex)
}

