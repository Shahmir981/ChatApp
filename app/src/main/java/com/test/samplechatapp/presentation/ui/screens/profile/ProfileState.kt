package com.test.samplechatapp.presentation.ui.screens.profile

import com.test.samplechatapp.domain.model.ProfileModel

sealed class ProfileState {
    data object Loading : ProfileState()
    data class Success(val userProfile: ProfileModel) : ProfileState()
    data class Error(val message: String) : ProfileState()
}
