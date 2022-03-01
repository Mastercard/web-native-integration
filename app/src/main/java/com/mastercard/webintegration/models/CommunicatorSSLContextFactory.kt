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

import android.util.Log
import okhttp3.OkHttpClient
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

/**
 * Default SSLContextFactory which will accept all certificate
 */
class CommunicatorSSLContextFactory : SSLContextFactory {
  @Throws(Exception::class)
  override fun build(x509Certificate: Certificate?): SSLContext {
    val sslContext = SSLContext.getInstance(TLS_PROTOCOL)
    if (x509Certificate != null) {
      val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
      keyStore.load(null, null)
      keyStore.setCertificateEntry(CERTIFICATE_ALIAS, x509Certificate)
      val tmf = TrustManagerFactory.getInstance(TRUST_MANAGER_ALGORITHM)
      tmf.init(keyStore)
      sslContext.init(null, tmf.trustManagers, null)
    } else {
      sslContext.init(null, null, null)
    }
    return sslContext
  }

  /**
   * This method returns trust all ssl okHttpClient
   *
   * @return OkHttpClient
   */
  val okHttpClient: OkHttpClient?
    get() {
      var okHttpClient: OkHttpClient? = null
      try {

        val trustManagerFactory =
          TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(null as KeyStore?)
        val trustManagers: Array<TrustManager> = trustManagerFactory.trustManagers
        check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
          "Unexpected default trust managers:" + trustManagers.contentToString()
        }
        val trustManager = trustManagers[0] as X509TrustManager

        // Install the all-trusting trust manager
        val sslContext = SSLContext.getInstance(TLS_PROTOCOL)
        sslContext.init(null, arrayOf<TrustManager>(trustManager), SecureRandom())

        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory = sslContext.socketFactory
        val builder = OkHttpClient.Builder()
        builder.sslSocketFactory(sslSocketFactory, trustManager)
        // builder.hostnameVerifier { hostname: String?, session: SSLSession? -> true }
        okHttpClient = builder.build()
      } catch (e: Exception) {
        e.printStackTrace()
        Log.d(TAG, "Exception while getting OkHttpClient: " + e.message)
      }
      return okHttpClient
    }


  companion object {
    private val TAG = CommunicatorSSLContextFactory::class.java.simpleName
    private const val TLS_PROTOCOL = "TLSv1.2"
    private const val SSL_PROTOCOL = "SSL"
    private const val TRUST_MANAGER_ALGORITHM = "X509"
    private const val CERTIFICATE_ALIAS = "ca"
  }
}