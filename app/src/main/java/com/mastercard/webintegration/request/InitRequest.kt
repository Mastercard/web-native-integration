package com.mastercard.webintegration.request

import com.google.gson.annotations.SerializedName

data class InitRequest(

	@field:SerializedName("cardBrands")
	val cardBrands: List<String?>? = null,

	@field:SerializedName("srcDpaId")
	val srcDpaId: String? = null,

	@field:SerializedName("dpaTransactionOptions")
	val dpaTransactionOptions: DpaTransactionOptions? = null,

	@field:SerializedName("dpaData")
	val dpaData: DpaData? = null
)

data class DpaData(

	@field:SerializedName("dpaPresentationName")
	val dpaPresentationName: String? = null,

	@field:SerializedName("dpaName")
	val dpaName: String? = null
)

data class DpaTransactionOptions(

	@field:SerializedName("dpaAcceptedShippingCountries")
	val dpaAcceptedShippingCountries: List<Any?>? = null,

	@field:SerializedName("consumerEmailAddressRequested")
	val consumerEmailAddressRequested: Boolean? = null,

	@field:SerializedName("threeDsPreference")
	val threeDsPreference: String? = null,

	@field:SerializedName("dpaShippingPreference")
	val dpaShippingPreference: String? = null,

	@field:SerializedName("consumerNameRequested")
	val consumerNameRequested: Boolean? = null,

	@field:SerializedName("paymentOptions")
	val paymentOptions: List<PaymentOptionsItem?>? = null,

	@field:SerializedName("consumerPhoneNumberRequested")
	val consumerPhoneNumberRequested: Boolean? = null,

	@field:SerializedName("transactionAmount")
	val transactionAmount: TransactionAmount? = null,

	@field:SerializedName("dpaAcceptedBillingCountries")
	val dpaAcceptedBillingCountries: List<Any?>? = null,

	@field:SerializedName("dpaLocale")
	val dpaLocale: String? = null,

	@field:SerializedName("dpaBillingPreference")
	val dpaBillingPreference: String? = null
)

data class PaymentOptionsItem(

	@field:SerializedName("dynamicDataType")
	val dynamicDataType: String? = null,

	@field:SerializedName("dpaDynamicDataTtlMinutes")
	val dpaDynamicDataTtlMinutes: Int? = null
)

data class TransactionAmount(

	@field:SerializedName("transactionAmount")
	val transactionAmount: Int? = null,

	@field:SerializedName("transactionCurrencyCode")
	val transactionCurrencyCode: String? = null
)
