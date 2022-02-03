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

package com.mastercard.webintegration

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import com.mastercard.webintegration.utils.Constants.ACTION_SHEET_MODE
import com.mastercard.webintegration.utils.Constants.ACTIVITY_NAME
import com.mastercard.webintegration.utils.Constants.MERCHANT_CONTEXT
import com.mastercard.webintegration.utils.Constants.METHOD_CHECKOUT_WITH_CARD
import com.mastercard.webintegration.utils.Constants.METHOD_CHECKOUT_WITH_NEW_CARD
import com.mastercard.webintegration.utils.Constants.METHOD_ENCRYPT_CARD
import com.mastercard.webintegration.utils.Constants.METHOD_GET_CARDS
import com.mastercard.webintegration.utils.Constants.METHOD_ID_LOOKUP
import com.mastercard.webintegration.utils.Constants.METHOD_INIT
import com.mastercard.webintegration.utils.Constants.METHOD_INITIATE_VALIDATION
import com.mastercard.webintegration.utils.Constants.METHOD_NAME
import com.mastercard.webintegration.utils.Constants.METHOD_VALIDATE
import com.mastercard.webintegration.R.layout
import kotlinx.android.synthetic.main.activity_webviewintegration.webView

/**
 * WebIntegrationActivity provides integration of Native layer with Web Javascript SDK.
 * This Activity checks the intent extras sent from MerchantActivity and determines which API method to be called on Mastercard JS SDK.
 * An optional JavascriptInterface implementation can be provided in this Activity to receive API response from Web JS SDK.
 */
class WebViewIntegrationActivity : Activity() {
  private val TAG: String = WebViewIntegrationActivity::class.java.simpleName
  private lateinit var merchantContext: String
  private lateinit var callingActivity: String

  private val filePath = "file:///android_asset/index.html"

  private val API_REQUEST =
    "API_REQUEST" //Request body required to invoke API. Example: init request body for init() API

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_webviewintegration);

    //MerchantActivity must pass MERCHANT_CONTEXT and ACTIVITY_NAME in the intent.extras while launching this Activity. Failing to pass these values will result in API response not being delivered back to caller.
    //MERCHANT_CONTEXT = Android package name of the Merchant Application. Example: com.merchant.app
    //ACTIVITY_NAME = Activity where Merchant Application is expecting the API Response. Example: MerchantActivity.localClassName
    val extras = intent.extras;
    if (extras?.getString(MERCHANT_CONTEXT) != null) {
      this.merchantContext = intent.extras?.getString(MERCHANT_CONTEXT)!!
      this.callingActivity = intent.extras?.getString(ACTIVITY_NAME)!!
    }

    val apiRequest = intent.extras?.getString(API_REQUEST);
    val methodName = intent.extras?.getString(METHOD_NAME);

    webView.settings.javaScriptEnabled = true
    WebView.setWebContentsDebuggingEnabled(true);

    //Add @Javascript
    webView.addJavascriptInterface(JSBridge(this@WebViewIntegrationActivity), "JSBridge")
    webView.settings.domStorageEnabled = true
    webView.setHorizontalScrollBarEnabled(false);

    //Cookies must be enabled to allow faster checkout of Returning users. Successful checkout will drop a cookie if user selects "Remember Me" during checkout.
    CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

    webView.webViewClient = object : WebViewClient() {
      override fun onPageFinished(
        view: WebView?,
        url: String?
      ) {
        super.onPageFinished(view, url)
        if (apiRequest != null && methodName != null) {
          callMethod(methodName, apiRequest, actionSheetMode = false)
        }
      }
    }
    webView.setBackgroundColor(Color.TRANSPARENT);
    webView.loadUrl(filePath)
  }

  override fun onStop() {
    Log.d(TAG, "ON STOP CALLED")
    if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
      CookieManager.getInstance().flush()
    } else {
      CookieSyncManager.getInstance().sync()
    }
    super.onStop()
  }

  override fun onBackPressed() {
    //Required to disable back press
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    setIntent(intent);

    if (getIntent().extras?.getString(MERCHANT_CONTEXT) != null) {
      val methodName = getIntent().extras!!.getString(METHOD_NAME)
      if (methodName == METHOD_CHECKOUT_WITH_CARD || methodName == METHOD_CHECKOUT_WITH_NEW_CARD) {
        // setTheme(style.ThemeOverlay_AppCompat_Light)
        webView.visibility = View.VISIBLE;
        val actionSheetMode = getIntent().extras?.getBoolean(ACTION_SHEET_MODE);

        callMethod(methodName, intent?.extras?.getString(API_REQUEST)!!, actionSheetMode)
      } else {
        if (methodName != null) {
          callMethod(methodName, intent?.extras?.getString(API_REQUEST)!!, actionSheetMode = false)
        }
      }
    }
  }

  private fun callMethod(
    method: String,
    apiRequest: String,
    actionSheetMode: Boolean?
  ) {
    when (method) {
      METHOD_INIT -> {
        webView.evaluateJavascript("initSdk(\"android\", JSON.stringify($apiRequest))", null)
      }
      METHOD_GET_CARDS -> {
        webView.evaluateJavascript("getCards(\"android\", JSON.stringify($apiRequest))", null)
      }
      METHOD_ENCRYPT_CARD -> {
        webView.evaluateJavascript("encryptCard(\"android\", JSON.stringify($apiRequest))", null)
      }
      METHOD_CHECKOUT_WITH_NEW_CARD -> {
        webView.evaluateJavascript(
          "checkoutWithNewCard(\"android\", JSON.stringify($apiRequest), $actionSheetMode)", null
        )
      }
      METHOD_ID_LOOKUP -> {
        webView.evaluateJavascript("idLookup(\"android\", JSON.stringify($apiRequest))", null)
      }
      METHOD_INITIATE_VALIDATION -> {
        webView.evaluateJavascript(
          "initiateValidation(\"android\", JSON.stringify($apiRequest))", null
        )
      }
      METHOD_VALIDATE -> {
        webView.evaluateJavascript("validate(\"android\", JSON.stringify($apiRequest))", null)
      }
      METHOD_CHECKOUT_WITH_CARD -> {
        webView.evaluateJavascript(
          "checkoutWithCard(\"android\", JSON.stringify($apiRequest), $actionSheetMode)", null
        )
      }
    }
  }

  /**
   * Receive message from webview and pass on to native.
   */
  class JSBridge(val context: Context) {
    private val DOT = "."
    private val INTENT_API_RESPONSE = "API_RESPONSE"
    private val INTENT_API_NAME = "API_NAME"

    @JavascriptInterface
    fun showMessageInNative(
      message: String,
      method: String
    ) {
      var activity: WebViewIntegrationActivity = context as WebViewIntegrationActivity
      activity.runOnUiThread(Runnable {

        val intent = Intent(Intent.ACTION_MAIN)
        intent.component = ComponentName(
          activity.merchantContext, activity.merchantContext + DOT + activity.callingActivity
        )
        intent.putExtra(INTENT_API_RESPONSE, message)
        intent.putExtra(INTENT_API_NAME, method)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        activity.startActivity(intent)
      });
    }
  }
}

