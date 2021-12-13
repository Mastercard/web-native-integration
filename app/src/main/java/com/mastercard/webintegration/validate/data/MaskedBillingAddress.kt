package com.mastercard.webintegration.validate.data

import com.google.gson.annotations.SerializedName

data class MaskedBillingAddress (
    @SerializedName("zip")
    val zip: String? = null,

    @SerializedName("city")
    val city: String? = null,

    @SerializedName("countryCode")
    val countryCode: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("state")
    val state: String? = null,

    @SerializedName("line3")
    val line3: Any? = null,

    @SerializedName("line2")
    val line2: Any? = null,

    @SerializedName("line1")
    val line1: String? = null,

    @SerializedName("addressId")
    val addressId: String? = null
    )