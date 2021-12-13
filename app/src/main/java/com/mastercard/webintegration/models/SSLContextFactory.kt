package com.mastercard.webintegration.models

import kotlin.Throws
import okhttp3.OkHttpClient
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception
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