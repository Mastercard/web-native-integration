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