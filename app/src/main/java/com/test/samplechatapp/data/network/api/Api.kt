package com.test.samplechatapp.data.network.api

import com.test.samplechatapp.data.network.dto.UpdateProfileDto
import com.test.samplechatapp.data.network.dto.ProfileDto
import com.test.samplechatapp.data.network.response.AuthResponse
import com.test.samplechatapp.data.network.requests.CodeRequest
import com.test.samplechatapp.data.network.requests.PhoneRequest
import com.test.samplechatapp.data.network.requests.RefreshTokenRequest
import com.test.samplechatapp.data.network.response.RefreshTokenResponse
import com.test.samplechatapp.data.network.requests.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface Api {
    @POST("/api/v1/users/send-auth-code/")
    suspend fun sendAuthCode(@Body request: PhoneRequest): Response<Unit>

    @POST("/api/v1/users/register/")
    suspend fun registerUser(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("/api/v1/users/refresh-token/")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<RefreshTokenResponse>

    @POST("/api/v1/users/check-auth-code/")
    suspend fun checkAuthCode(@Body request: CodeRequest): Response<AuthResponse>

    @GET("/api/v1/users/me/")
    suspend fun getUserProfile(): Response<ProfileDto>

    @PUT("/api/v1/users/me/")
    suspend fun updateProfile(@Body request: UpdateProfileDto): Response<Unit>
}
