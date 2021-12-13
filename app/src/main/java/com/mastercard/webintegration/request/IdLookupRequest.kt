package com.mastercard.webintegration.request

import com.google.gson.annotations.SerializedName

data class IdLookupRequest (
    @SerializedName("email")
    var email: String? = null
    )