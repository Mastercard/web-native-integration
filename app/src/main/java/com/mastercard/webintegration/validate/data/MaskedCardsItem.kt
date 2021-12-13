package com.mastercard.webintegration.validate.data

import com.google.gson.annotations.SerializedName

data class MaskedCardsItem (
    @SerializedName("tokenLastFour")
    val tokenLastFour: String? = null,

    @SerializedName("panBin")
    val panBin: String? = null,

    @SerializedName("dcf")
    val dcf: Dcf? = null,

    @SerializedName("digitalCardRelatedData")
    val digitalCardRelatedData: Any? = null,

    @SerializedName("digitalCardFeatures")
    val digitalCardFeatures: List<Any>? = null,

    @SerializedName("maskedBillingAddress")
    val maskedBillingAddress: MaskedBillingAddress? = null,

    @SerializedName("tokenBinRange")
    val tokenBinRange: Any? = null,

    @SerializedName("dateOfCardLastUsed")
    val dateOfCardLastUsed: String? = null,

    @SerializedName("countryCode")
    val countryCode: String? = null,

    @SerializedName("srcPaymentCardId")
    val srcPaymentCardId: Any? = null,

    @SerializedName("panLastFour")
    val panLastFour: String? = null,

    @SerializedName("digitalCardData")
    val digitalCardData: DigitalCardData? = null,

    @SerializedName("panExpirationYear")
    val panExpirationYear: String? = null,

    @SerializedName("srcDigitalCardId")
    val srcDigitalCardId: String? = null,

    @SerializedName("dateOfCardCreated")
    val dateOfCardCreated: String? = null,

    @SerializedName("panExpirationMonth")
    val panExpirationMonth: String? = null,

    @SerializedName("paymentCardDescriptor")
    val paymentCardDescriptor: String? = null
    )