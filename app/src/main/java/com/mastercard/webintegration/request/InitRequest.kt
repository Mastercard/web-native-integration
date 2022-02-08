/* Copyright © 2022 Mastercard. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 =============================================================================*/

package com.mastercard.webintegration.request

import com.google.gson.annotations.SerializedName

/**
 * Create an instance of this class with client specifications to invoke init API call.
 */
class InitRequest {
  @field:SerializedName("cardBrands")
  var cardBrands: MutableSet<String>? = null

  @field:SerializedName("srcDpaId")
  var srcDpaId: String? = null

  @field:SerializedName("dpaTransactionOptions")
  var dpaTransactionOptions: DpaTransactionOptions? = null

  @field:SerializedName("dpaData")
  var dpaData: DpaData? = null
}

class DpaData(
  @field:SerializedName("dpaPresentationName")
  val dpaPresentationName: String? = null,

  @field:SerializedName("dpaName")
  val dpaName: String? = null
)

class DpaTransactionOptions(

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

class PaymentOptionsItem(

  @field:SerializedName("dynamicDataType")
  val dynamicDataType: String? = null,

  @field:SerializedName("dpaDynamicDataTtlMinutes")
  val dpaDynamicDataTtlMinutes: Int? = null
)

class TransactionAmount(

  @field:SerializedName("transactionAmount")
  val transactionAmount: Int? = null,

  @field:SerializedName("transactionCurrencyCode")
  val transactionCurrencyCode: String? = null
)
