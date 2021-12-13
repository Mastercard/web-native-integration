package com.mastercard.webintegration.request

import com.google.gson.annotations.SerializedName

data class ValidateRequest (
    @SerializedName("value")
    var value: String? = null
    )