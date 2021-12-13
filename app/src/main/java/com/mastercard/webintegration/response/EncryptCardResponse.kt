package com.mastercard.webintegration.response

import com.google.gson.annotations.SerializedName

data class EncryptCardResponse (
    @SerializedName("encryptedCard")
    val encryptedCard: String? = null,

    @SerializedName("cardBrand")
    val cardBrand: String? = null
    )