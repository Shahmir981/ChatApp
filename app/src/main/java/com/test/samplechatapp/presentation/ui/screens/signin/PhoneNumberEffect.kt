package com.test.samplechatapp.presentation.ui.screens.signin

sealed class PhoneNumberEffect {
    data class ShowError(val message: String) : PhoneNumberEffect()
}
