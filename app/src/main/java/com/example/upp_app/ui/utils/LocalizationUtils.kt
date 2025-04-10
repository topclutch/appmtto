package com.example.upp_app.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.upp_app.ui.theme.ThemeManager

/**
 * Clase de utilidad para manejar la localización de textos en la aplicación
 */
object LocalizationUtils {

    // Mapa de traducciones para español
    private val spanishTranslations = mapOf(
        // Pantalla de bienvenida
        "welcome_title" to "BrainWave",
        "welcome_subtitle" to "Tu asistente de IA para conversaciones más inteligentes y productividad",
        "get_started" to "Comenzar",

        // Pantalla de inicio de sesión
        "sign_in" to "Iniciar Sesión",
        "sign_in_subtitle" to "¡Hola! Bienvenido de nuevo, te hemos extrañado",
        "email" to "Correo electrónico",
        "password" to "Contraseña",
        "forgot_password" to "¿Olvidaste tu contraseña?",
        "or_sign_in_with" to "O inicia sesión con",
        "dont_have_account" to "¿No tienes una cuenta?",
        "sign_up" to "Registrarse",

        // Pantalla de registro
        "create_account" to "Crear Cuenta",
        "register_subtitle" to "Completa tu información a continuación o regístrate con tu cuenta social.",
        "name" to "Nombre",
        "confirm_password" to "Confirmar Contraseña",
        "agree_terms" to "Acepto los ",
        "terms_condition" to "Términos y Condiciones",
        "or_sign_up_with" to "O regístrate con",
        "already_have_account" to "¿Ya tienes una cuenta?",

        // Pantalla de verificación
        "verify_code" to "Verificar Código",
        "verify_description" to "Por favor, ingresa el código que enviamos al correo electrónico",
        "didnt_receive_otp" to "¿No recibiste el código?",
        "resend_code" to "Reenviar código",
        "verify" to "Verificar",

        // Pantalla principal
        "dashboard" to "Panel Principal",
        "welcome_back" to "¡Bienvenido de nuevo",
        "what_to_do" to "¿Qué te gustaría hacer hoy?",
        "chatbot" to "Chatbot",
        "profile" to "Perfil",
        "settings" to "Configuración",
        "logout" to "Cerrar Sesión",

        // Pantalla de chatbot
        "ai_assistant" to "Asistente IA",
        "typing" to "Escribiendo...",
        "online" to "En línea",
        "type_message" to "Escribe un mensaje...",
        "hello_help" to "¡Hola! Soy tu asistente de IA. ¿En qué puedo ayudarte hoy?",

        // Pantalla de perfil
        "personal_information" to "Información Personal",
        "phone" to "Teléfono",
        "location" to "Ubicación",
        "joined" to "Se unió",
        "preferences" to "Preferencias",
        "language" to "Idioma",
        "theme" to "Tema",
        "notifications" to "Notificaciones",
        "sign_out" to "Cerrar Sesión",
        "edit_profile" to "Editar Perfil",
        "save" to "Guardar",
        "cancel" to "Cancelar",

        // Pantalla de configuración
        "account" to "Cuenta",
        "security" to "Seguridad",
        "subscription" to "Suscripción",
        "dark_mode" to "Modo Oscuro",
        "data_privacy" to "Datos y Privacidad",
        "data_usage" to "Uso de Datos",
        "privacy_mode" to "Modo Privacidad",
        "clear_data" to "Borrar Datos",
        "help_support" to "Ayuda y Soporte",
        "help_center" to "Centro de Ayuda",
        "send_feedback" to "Enviar Comentarios",
        "about" to "Acerca de",
        "confirm" to "Confirmar",

        // Menú de usuario
        "my_profile" to "Mi Perfil",
        "user" to "Usuario"
    )

    /**
     * Obtiene el texto localizado según el idioma actual
     */
    fun getLocalizedText(key: String): String {
        return if (ThemeManager.currentLanguage == "es") {
            spanishTranslations[key] ?: key
        } else {
            key
        }
    }
}

/**
 * Composable para obtener texto localizado
 */
@Composable
fun localizedString(key: String): String {
    return remember(key, ThemeManager.currentLanguage) {
        LocalizationUtils.getLocalizedText(key)
    }
}

