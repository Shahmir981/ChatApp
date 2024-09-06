package com.test.samplechatapp.domain.usecase.profile

import com.test.samplechatapp.data.network.utils.mappers.toUpdateProfileDto
import com.test.samplechatapp.domain.exception.AuthException
import com.test.samplechatapp.domain.exception.mapFailure
import com.test.samplechatapp.domain.model.UpdateProfileModel
import com.test.samplechatapp.domain.repository.AuthRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(profileRequest: UpdateProfileModel): Result<Unit> {
        return authRepository.updateUserProfileRequest(profileRequest.toUpdateProfileDto())
            .mapFailure { exception ->
                when (exception) {
                    is AuthException.Unauthorized -> AuthException.Unauthorized("Wrong verification code")
                    is AuthException.NotFound -> AuthException.NotFound()
                    is AuthException.NetworkError -> AuthException.NetworkError()
                    is AuthException.TokenRefreshFailed -> AuthException.TokenRefreshFailed()
                    else -> AuthException.UnknownError()
                }
            }
    }
}
