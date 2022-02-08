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
 * Request class to initiate checkout. Merchants must pass the following parameters depending on the
 * checkout flow <br></br>
 * 1. **New User Checkout:** This checkout flow allows card enrolment and checkout
 * simultaneously. ** encryptedCard ** is a required parameter to initiate this checkout flow.
 * <br></br>
 * 2. **Return User Checkout:** This checkout flow is for a returning user registered on SRC
 * system. *** srcDigitalCardId *** is a required parameter to initiate this checkout flow.
 */
data class CheckoutRequest(
  @SerializedName("srcDigitalCardId")
  var srcDigitalCardId: String? = null,

  @SerializedName("cardBrand")
  var cardBrand: String? = null,

  @SerializedName("encryptedCard")
  var encryptedCard: String? = null,

  @SerializedName("srciActionCode")
  var srciActionCode: String? = null,

  @SerializedName("windowRef")
  var windowRef: String? = null
)