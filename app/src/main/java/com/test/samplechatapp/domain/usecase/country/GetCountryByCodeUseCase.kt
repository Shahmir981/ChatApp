package com.test.samplechatapp.domain.usecase.country

import com.test.samplechatapp.data.network.utils.mappers.toCountryModel
import com.test.samplechatapp.domain.model.CountryModel
import com.test.samplechatapp.domain.repository.CountryRepository
import javax.inject.Inject


class GetCountryByCodeUseCase @Inject constructor(
    private val countryRepository: CountryRepository
) {
    fun invoke(code: String): CountryModel? {
        return countryRepository.getCountryByCode(code)?.toCountryModel()
    }
}
