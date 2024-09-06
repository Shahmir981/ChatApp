package com.test.samplechatapp.presentation.ui.screens.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.samplechatapp.domain.model.CountryModel
import com.test.samplechatapp.domain.usecase.auth.SendAuthCodeUseCase
import com.test.samplechatapp.domain.usecase.country.GetCountriesUseCase
import com.test.samplechatapp.domain.usecase.country.GetCountryByCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneNumberViewModel @Inject constructor(
    private val getCountriesUseCase: GetCountriesUseCase,
    private val getCountryByCodeUseCase: GetCountryByCodeUseCase,
    private val sendAuthCodeUseCase: SendAuthCodeUseCase
) : ViewModel() {

    private val _selectedCountry = MutableStateFlow<CountryModel?>(null)
    val selectedCountry: StateFlow<CountryModel?> = _selectedCountry

    private val _countries = MutableStateFlow<List<CountryModel>>(emptyList())
    val countries: StateFlow<List<CountryModel>> = _countries

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    private val _state = MutableStateFlow<PhoneNumberState>(PhoneNumberState.Initial)
    val state: StateFlow<PhoneNumberState> get() = _state

    init {
        loadCountries()
        selectDefaultCountry()
    }

    private fun loadCountries() {
        viewModelScope.launch {
            _countries.value = getCountriesUseCase.invoke()
        }
    }

    private fun selectDefaultCountry() {
        viewModelScope.launch {
            _selectedCountry.value = getCountryByCodeUseCase.invoke("RU")
        }
    }

    fun handleIntent(intent: PhoneNumberIntent) {
        when (intent) {
            is PhoneNumberIntent.SelectCountry -> selectCountry(intent.countryCode)
            is PhoneNumberIntent.SendPhoneNumber -> sendPhoneNumber()
            is PhoneNumberIntent.UpdatePhoneNumber -> updatePhoneNumber(intent.phoneNumber)
        }
    }

    private fun selectCountry(code: String) {
        viewModelScope.launch {
            _selectedCountry.value = getCountryByCodeUseCase.invoke(code)
        }
    }

    private fun updatePhoneNumber(number: String) {
        val cleanedNumber = number.replace("\\D".toRegex(), "")
        if (cleanedNumber.length <= 10) {
            _phoneNumber.value = cleanedNumber
        }
    }

    private fun sendPhoneNumber() {
        viewModelScope.launch {
            _state.value = PhoneNumberState.Loading
            try {
                sendAuthCodeUseCase(_phoneNumber.value)
                _state.value = PhoneNumberState.Success
            } catch (e: Exception) {
                _state.value = PhoneNumberState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
