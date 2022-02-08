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

package com.mastercard.webintegration.validate.data

import com.google.gson.annotations.SerializedName

data class MaskedCardsItem(
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