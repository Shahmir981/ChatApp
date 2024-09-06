package com.test.samplechatapp.presentation.ui.screens.verificationCode

sealed class VerificationCodeEffect {
    data object NavigateToChatList : VerificationCodeEffect()
    data class NavigateToRegistration(val phoneNumber: String) : VerificationCodeEffect()
    data class ShowErrorToast(val message: String) : VerificationCodeEffect()
}
