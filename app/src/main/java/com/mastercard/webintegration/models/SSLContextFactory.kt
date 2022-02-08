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

package com.mastercard.webintegration.models

import java.security.cert.Certificate
import javax.net.ssl.SSLContext

/**
 * SSLContextFactory interface to generate SSLContext
 */
interface SSLContextFactory {
  /**
   * Implement this method to create SSLContext
   * @param x509Certificate certificate used to generate SSLContext
   * @return
   * @throws Exception
   */
  @Throws(Exception::class)
  fun build(x509Certificate: Certificate?): SSLContext
}