package com.mastercard.webintegration.models

import android.util.Log
import kotlin.Throws
import okhttp3.OkHttpClient
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.Certificate
import java.security.cert.X509Certificate
import javax.net.ssl.*

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
            sslContext.init(null, trustAllCerts(), null)
        }
        return sslContext
    }// Install the all-trusting trust manager

    // Create an ssl socket factory with our all-trusting manager
    /**
     * This method returns trust all ssl okHttpClient
     *
     * @return OkHttpClient
     */
    val okHttpClient: OkHttpClient?
        get() {
            var okHttpClient: OkHttpClient? = null
            try {
                // Install the all-trusting trust manager
                val sslContext = SSLContext.getInstance(SSL_PROTOCOL)
                sslContext.init(null, trustAllCerts(), SecureRandom())

                // Create an ssl socket factory with our all-trusting manager
                val sslSocketFactory = sslContext.socketFactory
                val builder = OkHttpClient.Builder()
                builder.sslSocketFactory(sslSocketFactory, trustAllCerts()!![0] as X509TrustManager)
                builder.hostnameVerifier { hostname: String?, session: SSLSession? -> true }
                okHttpClient = builder.build()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d(TAG, "Exception while getting OkHttpClient: " + e.message)
            }
            return okHttpClient
        }

    /**
     * Trust all ssl certificate
     *
     * @return TrustManager[] accepted trust certificates will be returned
     */
    private fun trustAllCerts(): Array<TrustManager>? {
        var trustAllCerts: Array<TrustManager>? = null
        try {
            trustAllCerts = arrayOf(
                object : X509TrustManager {
                    override fun getAcceptedIssuers(): Array<X509Certificate?> {
                        return arrayOfNulls(0)
                    }

                    override fun checkClientTrusted(
                        certs: Array<X509Certificate>,
                        authType: String
                    ) {
                        // Trust all Client Certs
                    }

                    override fun checkServerTrusted(
                        certs: Array<X509Certificate>,
                        authType: String
                    ) {
                        // Trust all Server Certs
                    }
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "Exception while trusting certificate: " + e.message)
        }
        return trustAllCerts
    }

    companion object {
        private val TAG = CommunicatorSSLContextFactory::class.java.simpleName
        private const val TLS_PROTOCOL = "TLSv1.2"
        private const val SSL_PROTOCOL = "SSL"
        private const val TRUST_MANAGER_ALGORITHM = "X509"
        private const val CERTIFICATE_ALIAS = "ca"
    }
}