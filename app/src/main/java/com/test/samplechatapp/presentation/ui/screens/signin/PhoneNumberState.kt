package com.test.samplechatapp.presentation.ui.screens.signin

sealed class PhoneNumberState {
    data object Initial : PhoneNumberState()
    data object Loading : PhoneNumberState()
    data object Success : PhoneNumberState()
    data class Error(val message: String) : PhoneNumberState()
}
