package com.test.samplechatapp.presentation.ui.screens.registration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.samplechatapp.domain.usecase.auth.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<RegistrationState>(RegistrationState.Initial)
    val state: StateFlow<RegistrationState> get() = _state

    private val _effect = MutableSharedFlow<RegistrationEffect>()
    val effect: SharedFlow<RegistrationEffect> get() = _effect

    private val nameRegex = "^[A-Za-z ]*$".toRegex()

    private val usernameRegex = "^[A-Za-z0-9-_]*$".toRegex()

    var name by mutableStateOf("")
    var username by mutableStateOf("")

    fun handleIntent(intent: RegistrationIntent) {
        when (intent) {
            is RegistrationIntent.RegisterUser -> {
                viewModelScope.launch {
                    _state.value = RegistrationState.Loading
                    val result = registerUserUseCase(intent.phoneNumber, intent.name, intent.username)
                    result.fold(
                        onSuccess = {
                            _state.value = RegistrationState.Initial
                            _effect.emit(RegistrationEffect.NavigateToChatList)
                        },
                        onFailure = { exception ->
                            _state.value = RegistrationState.Initial
                            _effect.emit(RegistrationEffect.ShowError(exception.message ?: "Registration failed"))
                        }
                    )
                }
            }
        }
    }
    private fun isUsernameValid(username: String): Boolean {
        return username.matches(usernameRegex)
    }

    private fun isNameValid(name: String): Boolean {
        return name.matches(nameRegex)
    }

    fun onNameChange(newName: String) {
        name = if (isNameValid(newName)) newName else name
    }

    fun onUsernameChange(newUsername: String) {
        username = if (isUsernameValid(newUsername)) newUsername else username
    }
}
