package com.test.samplechatapp.presentation.ui.screens.profile

sealed class ProfileEffect {
    object NavigateToPhoneNumberScreen : ProfileEffect()
    object NavigateBack : ProfileEffect()

    data class ShowErrorMessage(val message: String) : ProfileEffect()
    object ProfileUpdated : ProfileEffect()
}
