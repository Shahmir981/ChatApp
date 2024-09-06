package com.test.samplechatapp.di

import android.content.Context
import android.content.SharedPreferences
import com.test.samplechatapp.data.network.api.Api
import com.test.samplechatapp.data.network.utils.interceptor.AuthInterceptor
import com.test.samplechatapp.data.repository.AuthRepositoryImpl
import com.test.samplechatapp.domain.provider.TokenProvider
import com.test.samplechatapp.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://plannerok.ru/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePlannerokApi(retrofit: Retrofit): Api {
        return retrofit.create(Api::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(preferences: SharedPreferences): AuthInterceptor {
        return AuthInterceptor(preferences)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addNetworkInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideTokenProvider(
        api: Api,
        preferences: SharedPreferences
    ): TokenProvider {
        return TokenProvider(api, preferences)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        api: Api,
        tokenProvider: TokenProvider,
    ): AuthRepository {
        return AuthRepositoryImpl(api, tokenProvider)
    }
}
