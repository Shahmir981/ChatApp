package com.test.samplechatapp.presentation.ui.screens.verificationCode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.samplechatapp.domain.usecase.auth.CheckAuthCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SmsVerificationCodeViewModel @Inject constructor(
    private val checkAuthCodeUseCase: CheckAuthCodeUseCase
) : ViewModel() {

    private val _effect = MutableSharedFlow<VerificationCodeEffect>()
    val effect: SharedFlow<VerificationCodeEffect> get() = _effect

    private val _state = MutableStateFlow<VerificationCodeState>(VerificationCodeState.Initial)
    val state: StateFlow<VerificationCodeState> get() = _state

    private val _otpValues = MutableStateFlow(List(6) { "" })
    val otpValues: StateFlow<List<String>> get() = _otpValues

    fun onOtpValueChange(index: Int, newValue: String) {
        if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
            _otpValues.value = _otpValues.value.toMutableList().apply {
                this[index] = newValue
            }
        }
    }

    fun verifyCode(phoneNumber: String) {
        val code = _otpValues.value.joinToString("")
        viewModelScope.launch {
            _state.value = VerificationCodeState.Loading
            try {
                val result = checkAuthCodeUseCase(phoneNumber, code)
                if (result.isSuccess) {
                    val authResult = result.getOrNull()
                    if (authResult?.isUserExists == true) {
                        _state.value = VerificationCodeState.Authenticated
                        _effect.emit(VerificationCodeEffect.NavigateToChatList)
                    } else {
                        _state.value = VerificationCodeState.Register
                        _effect.emit(VerificationCodeEffect.NavigateToRegistration(phoneNumber))
                    }
                } else {
                    _state.value = VerificationCodeState.Initial
                    _effect.emit(VerificationCodeEffect.ShowErrorToast(result.exceptionOrNull()?.message ?: "Wrong verification code"))
                }
            } catch (e: Exception) {
                _state.value = VerificationCodeState.Initial
                _effect.emit(VerificationCodeEffect.ShowErrorToast(e.message ?: "Unknown error"))
            }
        }
    }
}
