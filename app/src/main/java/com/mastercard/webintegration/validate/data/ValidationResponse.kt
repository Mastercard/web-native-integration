package com.mastercard.webintegration.validate.data

import com.google.gson.annotations.SerializedName

class ValidationResponse {
    @SerializedName("Response")
    val response: List<MaskedCardsItem>? = null
}