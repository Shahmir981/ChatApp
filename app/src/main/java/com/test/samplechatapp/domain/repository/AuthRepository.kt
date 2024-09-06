package com.test.samplechatapp.domain.repository

import com.test.samplechatapp.data.network.dto.ProfileDto
import com.test.samplechatapp.data.network.dto.UpdateProfileDto
import com.test.samplechatapp.data.network.requests.CodeRequest
import com.test.samplechatapp.data.network.requests.PhoneRequest
import com.test.samplechatapp.data.network.requests.RegisterRequest
import com.test.samplechatapp.data.network.response.AuthResponse

interface AuthRepository {
    suspend fun registerRequest(registerRequest: RegisterRequest): Result<AuthResponse>
    suspend fun sendAuthCodeRequest(phoneRequest: PhoneRequest): Result<Unit>
    suspend fun checkAuthCodeRequest(codeRequest: CodeRequest): Result<AuthResponse>
    suspend fun getUserProfileRequest(): Result<ProfileDto>
    suspend fun updateUserProfileRequest(profileRequest: UpdateProfileDto): Result<Unit>
    suspend fun logoutRequest()
}
