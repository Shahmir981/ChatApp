package com.test.samplechatapp.domain.usecase.auth

import com.test.samplechatapp.data.network.requests.PhoneRequest
import com.test.samplechatapp.domain.exception.AuthException
import com.test.samplechatapp.domain.exception.mapFailure
import com.test.samplechatapp.domain.repository.AuthRepository
import javax.inject.Inject

class SendAuthCodeUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(phone: String): Result<Unit> {
        return repository.sendAuthCodeRequest(PhoneRequest(phone)).mapFailure { exception ->
            when (exception) {
                is AuthException.Unauthorized -> AuthException.Unauthorized("Invalid verification code")
                is AuthException.NotFound -> AuthException.NotFound()
                is AuthException.NetworkError -> AuthException.NetworkError()
                is AuthException.TokenRefreshFailed -> AuthException.TokenRefreshFailed()
                else -> AuthException.UnknownError()
            }
        }
    }
}
