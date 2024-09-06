package com.test.samplechatapp.data.repository

import com.test.samplechatapp.data.network.api.Api
import com.test.samplechatapp.data.network.dto.ProfileDto
import com.test.samplechatapp.data.network.dto.UpdateProfileDto
import com.test.samplechatapp.data.network.response.AuthResponse
import com.test.samplechatapp.data.network.requests.CodeRequest
import com.test.samplechatapp.data.network.requests.PhoneRequest
import com.test.samplechatapp.data.network.requests.RegisterRequest
import com.test.samplechatapp.domain.exception.AuthException
import com.test.samplechatapp.domain.provider.TokenProvider
import com.test.samplechatapp.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: Api,
    private val tokenProvider: TokenProvider,
) : AuthRepository {

    private suspend fun <T> executeWithTokenRefresh(request: suspend () -> Response<T>): Result<T> {
        return try {
            val response = request()
            when {
                response.isSuccessful -> Result.success(response.body()!!)
                response.code() == 401 -> {
                    val newToken = tokenProvider.refreshAccessToken()
                    if (newToken != null) {
                        val newResponse = request()
                        if (newResponse.isSuccessful) {
                            Result.success(newResponse.body()!!)
                        } else {
                            Result.failure(AuthException.TokenRefreshFailed())
                        }
                    } else {
                        Result.failure(AuthException.TokenRefreshFailed())
                    }
                }
                response.code() == 404 -> Result.failure(AuthException.NotFound())
                else -> Result.failure(AuthException.UnknownError())
            }
        } catch (e: Exception) {
            Result.failure(AuthException.NetworkError())
        }
    }

    override suspend fun sendAuthCodeRequest(phoneRequest: PhoneRequest): Result<Unit> {
        return executeWithTokenRefresh {
            api.sendAuthCode(phoneRequest)
        }.map { Unit }
    }

    override suspend fun checkAuthCodeRequest(codeRequest: CodeRequest): Result<AuthResponse> {
        return executeWithTokenRefresh {
            api.checkAuthCode(codeRequest)
        }.onSuccess {
            tokenProvider.saveTokens(it.accessToken, it.refreshToken)
            tokenProvider.setUserLoggedIn(true)
        }
    }

    override suspend fun registerRequest(registerRequest: RegisterRequest): Result<AuthResponse> {
        return executeWithTokenRefresh {
            api.registerUser(registerRequest)
        }.onSuccess {
            tokenProvider.saveTokens(it.accessToken, it.refreshToken)
        }
    }

    override suspend fun getUserProfileRequest(): Result<ProfileDto> {
        return executeWithTokenRefresh {
            api.getUserProfile()
        }.mapCatching { response ->
            response.let {
                it
            }
        }.onFailure {
        }
    }

    override suspend fun updateUserProfileRequest(profileRequest: UpdateProfileDto): Result<Unit> {
        return executeWithTokenRefresh {
            api.updateProfile(profileRequest)
        }.map { }
    }

    override suspend fun logoutRequest() {
        withContext(Dispatchers.IO) {
            tokenProvider.clearTokens()
            tokenProvider.setUserLoggedIn(false)
        }
    }
}
