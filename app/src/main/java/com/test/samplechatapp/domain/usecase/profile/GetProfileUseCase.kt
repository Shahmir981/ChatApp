package com.test.samplechatapp.domain.usecase.profile

import com.test.samplechatapp.data.network.dto.ProfileDto
import com.test.samplechatapp.data.network.utils.mappers.toUserProfileModel
import com.test.samplechatapp.domain.exception.AuthException
import com.test.samplechatapp.domain.exception.mapFailure
import com.test.samplechatapp.domain.model.ProfileModel
import com.test.samplechatapp.domain.repository.AuthRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<ProfileModel> {
        return authRepository.getUserProfileRequest().mapCatching { userProfileDto: ProfileDto ->
            userProfileDto.toUserProfileModel()
        }.mapFailure { exception ->
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
