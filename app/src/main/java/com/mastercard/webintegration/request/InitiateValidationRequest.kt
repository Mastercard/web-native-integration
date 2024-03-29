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
 * Request class to initiate validation. Merchants can pass a validation channel (eg: Email, SMS),
 * if the user desires to receive an OTP by a specific channel.
 */
data class InitiateValidationRequest(
  @SerializedName("requestedValidationChannelId")
  var requestedValidationChannelId: String? = null
)