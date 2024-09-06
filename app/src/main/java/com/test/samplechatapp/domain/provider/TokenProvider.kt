package com.test.samplechatapp.domain.provider

import android.content.SharedPreferences
import com.test.samplechatapp.data.network.api.Api
import com.test.samplechatapp.data.network.requests.RefreshTokenRequest
import javax.inject.Inject

class TokenProvider @Inject constructor(
    private val api: Api,
    private val preferences: SharedPreferences
) {

    suspend fun refreshAccessToken(): String? {
        val refreshToken = preferences.getString(REFRESH_TOKEN, "")

        if (refreshToken.isNullOrEmpty()) {
            return null
        }

        return try {
            val response = api.refreshToken(RefreshTokenRequest(refreshToken))
            if (response.isSuccessful) {
                val newToken = response.body()?.accessToken
                val newRefreshToken = response.body()?.refreshToken

                if (newToken != null && newRefreshToken != null) {
                    saveTokens(newToken, newRefreshToken)
                    newToken
                } else {
                    null
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }


    fun saveTokens(accessToken: String?, refreshToken: String?) {
        preferences.edit()
            .putString(ACCESS_TOKEN, accessToken)
            .putString(REFRESH_TOKEN, refreshToken)
            .putLong(TOKEN_TIMESTAMP, System.currentTimeMillis())
            .apply()
    }

    fun setUserLoggedIn(isLoggedIn: Boolean) {
        preferences.edit()
            .putBoolean(IS_LOGGED_IN, isLoggedIn)
            .apply()
    }

    fun isUserLoggedIn(): Boolean {
        return preferences.getBoolean(IS_LOGGED_IN, false)
    }

    fun clearTokens() {
        preferences.edit()
            .remove(ACCESS_TOKEN)
            .remove(REFRESH_TOKEN)
            .remove(TOKEN_TIMESTAMP)
            .apply()
    }

    companion object {
        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"
        const val TOKEN_TIMESTAMP = "token_timestamp"
        const val IS_LOGGED_IN = "is_logged_in"
    }
}
