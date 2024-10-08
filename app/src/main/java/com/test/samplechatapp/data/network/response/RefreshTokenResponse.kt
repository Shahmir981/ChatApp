package com.test.samplechatapp.data.network.response

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse (
    @SerializedName("refresh_token")
    val refreshToken: String?,

    @SerializedName("access_token")
    val accessToken: String?,

    @SerializedName("user_id")
    val userId: Int?,
)
