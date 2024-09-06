package com.test.samplechatapp.data.network.utils.interceptor

import android.content.SharedPreferences
import com.test.samplechatapp.domain.provider.TokenProvider
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val preferences: SharedPreferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val baseRequest = chain.request()
        val url = baseRequest.url

        if (url.encodedPath.contains("/api/v1/users/register/") ||
            url.encodedPath.contains("/api/v1/users/send-auth-code/") ||
            url.encodedPath.contains("/api/v1/users/check-auth-code/")
        ) {
            return chain.proceed(baseRequest)
        }

        val token = preferences.getString(TokenProvider.ACCESS_TOKEN, "")

        val requestWithAuth = baseRequest.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(requestWithAuth)
    }
}
