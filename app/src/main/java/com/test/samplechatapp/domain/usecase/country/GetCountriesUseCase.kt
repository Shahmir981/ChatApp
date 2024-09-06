package com.test.samplechatapp.domain.usecase.country

import com.test.samplechatapp.data.network.utils.mappers.toCountryModel
import com.test.samplechatapp.domain.model.CountryModel
import com.test.samplechatapp.domain.repository.CountryRepository
import javax.inject.Inject

class GetCountriesUseCase@Inject constructor(
    private val countryRepository: CountryRepository
) {
    fun invoke(): List<CountryModel> {
        return countryRepository.getAllCountries().map { it.toCountryModel() }
    }
}
