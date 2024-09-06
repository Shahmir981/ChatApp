package com.test.samplechatapp.presentation.ui.screens.registration

sealed class RegistrationState {
    data object Initial : RegistrationState()
    data object Loading : RegistrationState()
    data class Error(val message: String) : RegistrationState()
    data class Success(val message: String) : RegistrationState()
}
