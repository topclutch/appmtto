package com.example.upp_app.ui.screen.login

import android.content.Intent
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import com.example.upp_app.R
import com.example.upp_app.api.AuthManager
import com.example.upp_app.ui.components.InputField
import com.example.upp_app.ui.components.Link
import com.example.upp_app.ui.components.Message
import com.example.upp_app.ui.components.PrimaryButton
import com.example.upp_app.ui.components.SocialButton
import com.example.upp_app.ui.theme.Background
import com.example.upp_app.ui.theme.ErrorColor
import com.example.upp_app.ui.theme.MediumTextColor
import com.example.upp_app.ui.theme.SuccessColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.text.KeyboardOptions
import com.example.upp_app.ui.utils.LocalActivity
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricManager

import android.provider.Settings

// Función auxiliar para lanzar el Intent de enrolamiento
private fun promptEnroll(activity: FragmentActivity) {
    val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
        putExtra(
            Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
            BiometricManager.Authenticators.BIOMETRIC_STRONG
        )
    }
    activity.startActivity(enrollIntent)
}
@Composable
fun LoginScreen(
    navController: NavController,
    authManager: AuthManager,  // Agrega este parámetro aquí
    modifier: Modifier = Modifier
) {
    // Estados para email, contraseña y error
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var forgotPasswordEmail by remember { mutableStateOf("") }
    var forgotPasswordMessage by remember { mutableStateOf("") }
    var isSendingResetEmail by remember { mutableStateOf(false) }
    var showBiometricOption by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val authManager = remember { AuthManager(context) }
    val coroutineScope = rememberCoroutineScope()
    val activity = LocalActivity.current

    // Check if biometric authentication is available
    showBiometricOption = authManager.isBiometricAvailable() && authManager.isBiometricEnabled()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // Mensaje de bienvenida
        Message(
            title = "Sign In",
            subtitle = "Hi! Welcome back, you've been missed"
        )

        Spacer(modifier = Modifier.height(32.dp))

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
            trailingIcon = null,
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

        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .align(Alignment.End)
        ) {
            Link(text = "Forgot Password?") {
                showForgotPasswordDialog = true
                forgotPasswordEmail = email // Pre-llenar con el email si ya está ingresado
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

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de inicio de sesión con Laravel API
        PrimaryButton(
            onClick = {
                // Validar campos antes de intentar autenticar
                errorMessage = ""

                when {
                    email.isEmpty() -> {
                        errorMessage = "Please enter your email"
                    }

                    password.isEmpty() -> {
                        errorMessage = "Please enter your password"
                    }

                    else -> {
                        isLoading = true
                        // Se usa una corrutina para manejar la autenticación
                        coroutineScope.launch {
                            try {
                                val result = authManager.login(email, password)
                                isLoading = false

                                if (result.isSuccess) {
                                    // Inicio de sesión exitoso, navega a la pantalla "home"
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                } else {
                                    // Si ocurre un error, se muestra el mensaje correspondiente
                                    errorMessage = when {
                                        result.exceptionOrNull()?.message?.contains("no user record") == true ->
                                            "No account found with this email"

                                        result.exceptionOrNull()?.message?.contains("password is invalid") == true ->
                                            "Incorrect password"

                                        result.exceptionOrNull()?.message?.contains("network error") == true ->
                                            "Network error. Please check your connection"

                                        else -> result.exceptionOrNull()?.message
                                            ?: "Authentication failed"
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
            text = if (isLoading) "Signing In..." else "Sign In",
            modifier = Modifier.padding(horizontal = 16.dp),
            enabled = !isLoading
        )

        // Biometric authentication button (if available)
        if (showBiometricOption) {
            Spacer(modifier = Modifier.height(16.dp))

            PrimaryButton(
                onClick = {
                    val activity = context as? FragmentActivity
                    if (activity != null) {
                        authManager.showBiometricPrompt(
                            activity = activity,
                            onSuccess = { result ->
                                // User authenticated with biometrics
                                coroutineScope.launch {
                                    isLoading = true
                                    try {
                                        // Use stored email for biometric login
                                        val storedEmail = authManager.getUserEmail() ?: ""
                                        if (storedEmail.isNotEmpty()) {
                                            val loginResult = authManager.login(storedEmail, "")
                                            isLoading = false

                                            if (loginResult.isSuccess) {
                                                navController.navigate("home") {
                                                    popUpTo("login") { inclusive = true }
                                                }
                                            } else {
                                                errorMessage =
                                                    "Biometric authentication failed. Please try again or use email/password."
                                            }
                                        } else {
                                            isLoading = false
                                            errorMessage =
                                                "No account found for biometric login. Please use email/password."
                                        }
                                    } catch (e: Exception) {
                                        isLoading = false
                                        errorMessage = e.message ?: "An unexpected error occurred"
                                    }
                                }
                            },
                            onError = { errorCode, errString ->
                                errorMessage = "Biometric authentication error: $errString"
                            }
                        )
                    }
                },
                text = "Sign In with Fingerprint",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Opciones de inicio de sesión con redes sociales
        Text(
            text = "Or sign in with",
            style = MaterialTheme.typography.bodyMedium,
            color = MediumTextColor,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SocialButton(
                iconResId = R.drawable.ic_apple,
                contentDescription = "Sign in with Apple",
                onClick = { /* Implementar inicio de sesión con Apple */ }
            )

            Spacer(modifier = Modifier.width(24.dp))

            SocialButton(
                iconResId = R.drawable.ic_google,
                contentDescription = "Sign in with Google",
                onClick = { /* Implementar inicio de sesión con Google */ }
            )

            Spacer(modifier = Modifier.width(24.dp))

            SocialButton(
                iconResId = R.drawable.ic_facebook,
                contentDescription = "Sign in with Facebook",
                onClick = { /* Implementar inicio de sesión con Facebook */ }
            )
            Spacer(modifier = Modifier.width(24.dp))
            SocialButton(
                iconResId = R.drawable.ic_fingerprint,
                contentDescription = "Sign in with Fingerprint",
                onClick = {
                    val activity = context as? FragmentActivity
                    if (activity == null) {
                        errorMessage = "No se pudo obtener el contexto de la actividad"
                        return@SocialButton
                    }

                    // 1. Comprueba disponibilidad y enrolamiento
                    val biometricManager = BiometricManager.from(context)
                    when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
                        BiometricManager.BIOMETRIC_SUCCESS -> {
                            // Todo OK: muestra el prompt
                            authManager.showBiometricPrompt(
                                activity = activity,
                                onSuccess = { result ->
                                    // igual que antes...
                                },
                                onError = { errorCode, errString ->
                                    if (errorCode == BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED) {
                                        // Código 11 en algunas versiones
                                        promptEnroll(activity)
                                    } else {
                                        errorMessage = "Biometric error: $errString"
                                    }
                                }
                            )
                        }

                        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                            errorMessage = "Este dispositivo no soporta biometría"

                        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                            errorMessage = "Sensor biométrico no disponible"

                        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                            // No hay huellas/rostro registrado
                            promptEnroll(activity)
                        }
                    }
                }

            )
        }

            Spacer(modifier = Modifier.weight(1f))

            // Enlace para registrarse
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Text(
                    text = "Don't have an account?",
                    color = MediumTextColor
                )
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                Link(text = "Sign Up") {
                    navController.navigate("register")
                }
            }
        }

        // Diálogo de "Olvidé mi contraseña"
        if (showForgotPasswordDialog) {
            AlertDialog(
                onDismissRequest = {
                    showForgotPasswordDialog = false
                    forgotPasswordMessage = ""
                },
                title = { Text("Reset Password") },
                text = {
                    Column {
                        Text("Enter your email address and we'll send you a link to reset your password.")

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = forgotPasswordEmail,
                            onValueChange = { forgotPasswordEmail = it },
                            placeholder = { Text("Email") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (forgotPasswordMessage.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = forgotPasswordMessage,
                                color = if (forgotPasswordMessage.contains("sent") ||
                                    forgotPasswordMessage.contains("success")
                                )
                                    SuccessColor else ErrorColor,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        if (isSendingResetEmail) {
                            Spacer(modifier = Modifier.height(8.dp))
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.CenterHorizontally),
                                strokeWidth = 2.dp
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (forgotPasswordEmail.isEmpty()) {
                                forgotPasswordMessage = "Please enter your email address"
                                return@TextButton
                            }

                            isSendingResetEmail = true
                            forgotPasswordMessage = ""

                            coroutineScope.launch {
                                try {
                                    val result =
                                        authManager.sendPasswordResetEmail(forgotPasswordEmail)
                                    isSendingResetEmail = false

                                    if (result.isSuccess) {
                                        forgotPasswordMessage =
                                            "Password reset email sent successfully"
                                        // Cerrar el diálogo después de un tiempo
                                        coroutineScope.launch {
                                            delay(2000)
                                            showForgotPasswordDialog = false
                                            forgotPasswordMessage = ""
                                        }
                                    } else {
                                        forgotPasswordMessage = result.exceptionOrNull()?.message
                                            ?: "Failed to send reset email"
                                    }
                                } catch (e: Exception) {
                                    isSendingResetEmail = false
                                    forgotPasswordMessage =
                                        e.message ?: "An unexpected error occurred"
                                }
                            }
                        },
                        enabled = !isSendingResetEmail
                    ) {
                        Text("Send Reset Link")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showForgotPasswordDialog = false
                            forgotPasswordMessage = ""
                        },
                        enabled = !isSendingResetEmail
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }




