package com.test.samplechatapp.presentation.ui.screens.verificationCode

sealed class VerificationCodeState {
    data object Initial : VerificationCodeState()
    data object Register : VerificationCodeState()
    data object Authenticated : VerificationCodeState()
    data object Loading : VerificationCodeState()
    data class Error(val message: String) : VerificationCodeState()
}
