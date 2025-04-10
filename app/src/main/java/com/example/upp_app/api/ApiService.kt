package com.example.upp_app.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthResponse>

    @POST("users")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<AuthResponse>

    @POST("verify-biometric")
    suspend fun verifyBiometric(@Body biometricRequest: BiometricRequest): Response<AuthResponse>

    @GET("user/{id}")
    suspend fun getUserProfile(@Path("id") userId: Int): Response<UserResponse>

    @PUT("user/{id}")
    suspend fun updateUserProfile(
        @Path("id") userId: Int,
        @Body updateRequest: UpdateProfileRequest
    ): Response<UserResponse>

    @POST("password/email")
    suspend fun sendPasswordResetEmail(@Body resetRequest: PasswordResetRequest): Response<MessageResponse>
}