package com.mastercard.webintegration.validate.data

import com.google.gson.annotations.SerializedName

data class DigitalCardData (
    @SerializedName("presentationName")
    val presentationName: String? = null,

    @SerializedName("isCoBranded")
    val isIsCoBranded: Boolean = false,

    @SerializedName("artWidth")
    val artWidth: Any? = null,

    @SerializedName("artUri")
    val artUri: String? = null,

    @SerializedName("descriptorName")
    val descriptorName: String? = null,

    @SerializedName("artHeight")
    val artHeight: Any? = null,

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("pendingEvents")
    val pendingEvents: Any? = null,

    @SerializedName("coBrandedName")
    val coBrandedName: Any? = null
    )