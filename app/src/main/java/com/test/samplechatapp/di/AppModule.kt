package com.test.samplechatapp.di

import com.test.samplechatapp.data.repository.ChatRepositoryImpl
import com.test.samplechatapp.data.repository.CountryRepositoryImpl
import com.test.samplechatapp.domain.repository.ChatRepository
import com.test.samplechatapp.domain.repository.CountryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideChatRepository() : ChatRepository {
        return ChatRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideCountryRepository(): CountryRepository {
        return CountryRepositoryImpl()
    }
}
