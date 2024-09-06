package com.test.samplechatapp.domain.usecase.auth

import com.test.samplechatapp.data.network.requests.CodeRequest
import com.test.samplechatapp.data.network.response.AuthResponse
import com.test.samplechatapp.domain.exception.AuthException
import com.test.samplechatapp.domain.exception.mapFailure
import com.test.samplechatapp.domain.repository.AuthRepository
import javax.inject.Inject

class CheckAuthCodeUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(phone: String, code: String): Result<AuthResponse> {
        return authRepository.checkAuthCodeRequest(CodeRequest(phone = phone, code = code))
            .mapFailure { exception ->
                when {
                    exception.message?.contains("404") == true -> AuthException.Unauthorized("Wrong verification code") // error handling
                    else -> AuthException.UnknownError()
                }
            }
    }
}
