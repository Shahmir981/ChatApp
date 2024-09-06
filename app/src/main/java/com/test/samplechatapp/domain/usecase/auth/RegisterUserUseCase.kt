package com.test.samplechatapp.domain.usecase.auth

import com.test.samplechatapp.data.network.requests.RegisterRequest
import com.test.samplechatapp.data.network.response.AuthResponse
import com.test.samplechatapp.domain.exception.AuthException
import com.test.samplechatapp.domain.exception.mapFailure
import com.test.samplechatapp.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(phoneNumber: String, name: String, username: String): Result<AuthResponse> {
        return authRepository.registerRequest(RegisterRequest(phoneNumber, name, username))
            .mapFailure { exception ->
                when (exception) {
                    is AuthException.Unauthorized -> Exception("Authentication failed")
                    is AuthException.NotFound -> Exception("Unable to find")
                    is AuthException.NetworkError -> Exception("Network error")
                    is AuthException.TokenRefreshFailed -> Exception("Token refresh failed")
                    else -> Exception("Unknown error")
                }
            }
    }
}
