package com.test.samplechatapp.presentation

import androidx.lifecycle.ViewModel
import com.test.samplechatapp.domain.provider.TokenProvider
import com.test.samplechatapp.presentation.ui.components.navigation.ScreenType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(tokenProvider: TokenProvider) : ViewModel() {
    private val _startDestination = MutableStateFlow(ScreenType.LoginGraph.route)
    val startDestination: StateFlow<String> get() = _startDestination

    init {
        val screen = if (tokenProvider.isUserLoggedIn()) {
            ScreenType.ChatsGraph
        } else {
            ScreenType.LoginGraph
        }

        updateStartDestination(screen.route)
    }

    fun updateStartDestination(value: String) {
        _startDestination.value = value
    }
}
