package com.test.samplechatapp.domain.usecase.auth

import com.test.samplechatapp.domain.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        authRepository.logoutRequest()
    }
}
