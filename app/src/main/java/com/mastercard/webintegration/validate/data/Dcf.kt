package com.mastercard.webintegration.validate.data

import com.google.gson.annotations.SerializedName

data class Dcf (
    @SerializedName("name")
    val name: String? = null,

    @SerializedName("logoUri")
    val logoUri: String? = null,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("uri")
    val uri: String? = null
    )