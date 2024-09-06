package com.test.samplechatapp.data.repository

import com.test.samplechatapp.R
import com.test.samplechatapp.data.network.dto.CountryDto
import com.test.samplechatapp.domain.repository.CountryRepository
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor() : CountryRepository {
    private val countries = listOf(
        CountryDto("Georgia", "GA", "+995", R.drawable.flag_of_georgia),
        CountryDto("Armenia", "AM", "+374", R.drawable.flag_of_armenia),
        CountryDto("Russia", "RU", "+7", R.drawable.flag_of_russia)
    )

    override fun getAllCountries(): List<CountryDto> {
        return countries
    }

    override fun getCountryByCode(code: String): CountryDto? {
        return countries.find { it.code == code }
    }
}

