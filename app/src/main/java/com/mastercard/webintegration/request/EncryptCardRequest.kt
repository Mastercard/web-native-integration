package com.mastercard.webintegration.request

import com.google.gson.annotations.SerializedName
import com.mastercard.webintegration.validate.data.MaskedBillingAddress

data class EncryptCardRequest (
    @SerializedName("cardBrand")
    var cardBrand: String? = null,

    @SerializedName("primaryAccountNumber")
    var cardNumber: String? = null,

    @SerializedName("panExpirationYear")
    var panExpirationYear: String? = null,

    @SerializedName("panExpirationMonth")
    var panExpirationMonth: String? = null,

    @SerializedName("cardSecurityCode")
    var cardSecurityCode: String? = null,

    @SerializedName("cardholderFirstName")
    var cardholderFirstName: String? = null,

    @SerializedName("cardholderLastName")
    var cardholderLastName: String? = null,

    @SerializedName("billingAddress")
    var billingAddress: MaskedBillingAddress? = null
    )