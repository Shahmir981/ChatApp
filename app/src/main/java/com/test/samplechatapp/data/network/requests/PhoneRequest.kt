package com.test.samplechatapp.data.network.requests

import com.google.gson.annotations.SerializedName

data class PhoneRequest(
    @SerializedName("phone")
    val phone: String
)
