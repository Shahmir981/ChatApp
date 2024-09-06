package com.test.samplechatapp.domain.repository

import com.test.samplechatapp.data.network.dto.CountryDto

interface CountryRepository {
    fun getAllCountries(): List<CountryDto>
    fun getCountryByCode(code: String): CountryDto?
}
