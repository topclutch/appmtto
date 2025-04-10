package com.example.upp_app.api

// Request models
data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,
    val password_confirmation: String
)

data class BiometricRequest(
    val email: String,
    val biometric_token: String
)

data class UpdateProfileRequest(
    val name: String,
    val email: String,
    val password: String? = null,
    val password_confirmation: String? = null
)

data class PasswordResetRequest(
    val email: String
)

// Response models
data class AuthResponse(
    val user: User,
    val token: String,
    val message: String
)

data class UserResponse(
    val user: User,
    val message: String? = null
)

data class MessageResponse(
    val message: String
)

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val email_verified_at: String?,
    val created_at: String,
    val updated_at: String
)

