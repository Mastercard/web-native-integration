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

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mastercard.webintegration.Constants.ACTION_SHEET_MODE
import com.mastercard.webintegration.Constants.ACTIVITY_NAME
import com.mastercard.webintegration.Constants.API_REQUEST
import com.mastercard.webintegration.Constants.INTENT_API_NAME
import com.mastercard.webintegration.Constants.INTENT_API_RESPONSE
import com.mastercard.webintegration.Constants.MERCHANT_CONTEXT
import com.mastercard.webintegration.Constants.METHOD_CHECKOUT_WITH_CARD
import com.mastercard.webintegration.Constants.METHOD_CHECKOUT_WITH_NEW_CARD
import com.mastercard.webintegration.Constants.METHOD_ENCRYPT_CARD
import com.mastercard.webintegration.Constants.METHOD_GET_CARDS
import com.mastercard.webintegration.Constants.METHOD_ID_LOOKUP
import com.mastercard.webintegration.Constants.METHOD_INIT
import com.mastercard.webintegration.Constants.METHOD_INITIATE_VALIDATION
import com.mastercard.webintegration.Constants.METHOD_NAME
import com.mastercard.webintegration.Constants.METHOD_VALIDATE
import com.mastercard.webintegration.R.drawable
import com.mastercard.webintegration.R.id
import com.mastercard.webintegration.R.layout
import com.mastercard.webintegration.models.CommunicatorSSLContextFactory
import com.mastercard.webintegration.models.DividerItemDecorator
import com.mastercard.webintegration.request.CheckoutRequest
import com.mastercard.webintegration.request.EncryptCardRequest
import com.mastercard.webintegration.request.IdLookupRequest
import com.mastercard.webintegration.request.InitRequest
import com.mastercard.webintegration.request.InitiateValidationRequest
import com.mastercard.webintegration.request.ValidateRequest
import com.mastercard.webintegration.response.EncryptCardResponse
import com.mastercard.webintegration.ui.ProgressDialogFragment
import com.mastercard.webintegration.utils.DisplayView
import com.mastercard.webintegration.validate.adapter.CardAdapter
import com.mastercard.webintegration.validate.data.MaskedBillingAddress
import com.mastercard.webintegration.validate.data.MaskedCardsItem
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.Builder
import kotlinx.android.synthetic.main.activity_main.actionSheetCB
import kotlinx.android.synthetic.main.activity_main.amexCrdCB
import kotlinx.android.synthetic.main.activity_main.cardNumberText
import kotlinx.android.synthetic.main.activity_main.checkoutBtn
import kotlinx.android.synthetic.main.activity_main.checkoutResponse
import kotlinx.android.synthetic.main.activity_main.checkoutWithCardRequest
import kotlinx.android.synthetic.main.activity_main.checkoutWithNewCardBtn
import kotlinx.android.synthetic.main.activity_main.checkoutWithNewCardRequest
import kotlinx.android.synthetic.main.activity_main.checkoutWithNewCardResponse
import kotlinx.android.synthetic.main.activity_main.discoverCrdCB
import kotlinx.android.synthetic.main.activity_main.encryptCardBtn
import kotlinx.android.synthetic.main.activity_main.encryptedCardRequest
import kotlinx.android.synthetic.main.activity_main.encryptedCardResponse
import kotlinx.android.synthetic.main.activity_main.expiryDateText
import kotlinx.android.synthetic.main.activity_main.getCardsBtn
import kotlinx.android.synthetic.main.activity_main.getCardsResponse
import kotlinx.android.synthetic.main.activity_main.idLookupBtn
import kotlinx.android.synthetic.main.activity_main.idLookupEmail
import kotlinx.android.synthetic.main.activity_main.idLookupRequest
import kotlinx.android.synthetic.main.activity_main.idLookupResponse
import kotlinx.android.synthetic.main.activity_main.initButton
import kotlinx.android.synthetic.main.activity_main.initRequest
import kotlinx.android.synthetic.main.activity_main.initResponse
import kotlinx.android.synthetic.main.activity_main.initValidSelector
import kotlinx.android.synthetic.main.activity_main.initiateValidationBtn
import kotlinx.android.synthetic.main.activity_main.initiateValidationResponse
import kotlinx.android.synthetic.main.activity_main.masterCrdCB
import kotlinx.android.synthetic.main.activity_main.securityCodeText
import kotlinx.android.synthetic.main.activity_main.validateBtn
import kotlinx.android.synthetic.main.activity_main.validateOtp
import kotlinx.android.synthetic.main.activity_main.validateRequest
import kotlinx.android.synthetic.main.activity_main.validateResponseText
import kotlinx.android.synthetic.main.activity_main.visaCrdCB
import okhttp3.OkHttpClient
import java.io.IOException
import java.lang.reflect.Type
import java.util.regex.Pattern

/**
 * Sample Android activity which performs native-side operations to interact with Javascript SDK APIs.
 */
class MainActivity : AppCompatActivity(), DisplayView, AdapterView.OnItemSelectedListener {
  /**
   * Sample Merchant Activity showing integration of Click-to-Pay checkout experience.
   */

  var encryptedCard: String = ""
  lateinit var validationResponse: List<MaskedCardsItem>
  var srciDigitalCardId: String = ""
  lateinit var recyclerView: RecyclerView
  var validateOptionsArray = arrayOf("None", "Email", "SMS")

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_main)

    initializeView()
    addTextChangeListenerForCardExpiry()

    initButton.setOnClickListener {
      if (masterCrdCB.isChecked || amexCrdCB.isChecked || discoverCrdCB.isChecked || visaCrdCB.isChecked) {
        init()
      } else {
        showMessageDialog(MESSAGE)
      }
    }

    encryptCardBtn.setOnClickListener {
      window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
      if (cardNumberText.text.toString().trim().isNotEmpty() &&
        expiryDateText.text.toString().trim().isNotEmpty() &&
        securityCodeText.text.toString().trim().isNotEmpty()
      ) {
        showProgressDialog()
        encryptCard()
      }

    }

    checkoutWithNewCardBtn.setOnClickListener {
      if(encryptedCard.isEmpty()) {
        showMessageDialog(resources.getString(R.string.card_encryption_error))
      } else {
        showProgressDialog()
        checkoutWithNewCard()
      }
    }

    getCardsBtn.setOnClickListener {
      if(!initResponse.text.isEmpty()) {
        showProgressDialog()
        getCards()
      }
    }

    idLookupBtn.setOnClickListener {
      window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
      if (idLookupEmail.text.toString().trim()
          .isNotEmpty() && isValidEmail(idLookupEmail.text.toString().trim())
      ) {
        showProgressDialog()
        idLookup()
      }
    }

    initiateValidationBtn.setOnClickListener {
      showProgressDialog()
      initiateValidation(initValidSelector.selectedItem.toString())
    }

    validateBtn.setOnClickListener {
      window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
      if (validateOtp.text.toString().trim()
          .isNotEmpty() && validateOtp.text.toString().length == OTP_LENGTH
      ) {
        showProgressDialog()
        validate()
      }
    }

    checkoutBtn.setOnClickListener(View.OnClickListener {
      if(srciDigitalCardId.isEmpty()) {
        showMessageDialog(resources.getString(R.string.select_card_message))
      } else {
        showProgressDialog()
        checkoutWithCard()
      }
    })
  }

  override fun onBackPressed() {
    //Required to disable back press
  }

  /**
   * Receive API response from [WebViewIntegrationActivity]
   */
  override fun onNewIntent(intent: Intent?) {
    hideProgressDialog()
    super.onNewIntent(intent)
    val apiName = intent!!.getStringExtra(INTENT_API_NAME)
    val resultExtra = intent.getStringExtra(INTENT_API_RESPONSE)

    when (apiName) {
      "init" -> initResponse.text = resultExtra
      "getCards" -> {
        getCardsResponse.text = resultExtra
        processGetCards(resultExtra)
      }
      "encryptCard" -> {
        encryptedCard = resultExtra
        encryptedCardResponse.text = resultExtra
      }
      "checkoutWithNewCard" -> checkoutWithNewCardResponse.text = resultExtra
      "idLookup" -> {
        idLookupResponse.text = resultExtra
      }
      "initiateValidation" -> initiateValidationResponse.text = resultExtra
      "validate" -> {
        val gson = Gson()
        val type: Type = object : TypeToken<List<MaskedCardsItem>>() {}.type

        validationResponse = gson.fromJson(resultExtra, type)
        validateResponseText.text = resultExtra
        setCardList(validationResponse)
      }
      "errorValidate" -> {
        validateResponseText.text = resultExtra
      }
      "checkoutWithCard" -> checkoutResponse.text = resultExtra
    }
  }

  private fun initializeView() {
    //val recognizedUserLayout: LinearLayout = findViewById(id.recognizedUserLayout)
    recyclerView = findViewById(id.card_recycler_view)
    recyclerView.layoutManager = LinearLayoutManager(this)

    //Set layout to use when the list of choices appear
    val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, validateOptionsArray)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    initValidSelector.adapter = adapter
  }

  /**
   * Send data to webview through function updateFromNative.
   */
  private fun init() {
    val gson = Gson()
    clearTextFields()
    val jsonFileString = getJsonDataFromAsset(applicationContext, "initRequest.json")

    val initRequestObject =
      gson.fromJson(jsonFileString, InitRequest::class.java)

    if (initRequestObject.cardBrands != null) {
      initRequestObject.cardBrands!!.clear()
    }

    var selectedNetworks = LinkedHashSet<String>()

    if (masterCrdCB.isChecked) {
      selectedNetworks.add(getString(R.string.mastercard).lowercase())
    }
    if (visaCrdCB.isChecked) {
      selectedNetworks.add(getString(R.string.visa).lowercase())
    }

    if (amexCrdCB.isChecked) {
      selectedNetworks.add(getString(R.string.amex).lowercase())
    }

    if (discoverCrdCB.isChecked) {
      selectedNetworks.add(getString(R.string.discover).lowercase())
    }

    initRequestObject.cardBrands?.addAll(selectedNetworks)

    if (initRequestObject.srcDpaId?.isEmpty()!!) {
      showMessageDialog(INIT_ERROR_MESSAGE)
    } else {
      showProgressDialog()
      val intent = Intent(this, WebViewIntegrationActivity::class.java)
      val initRequestJson = gson.toJson(initRequestObject)
      initRequest.text = initRequestJson
      Log.d(TAG, "InitRequest: $initRequestJson")

      intent.putExtra(API_REQUEST, initRequestJson)
      intent.putExtra(MERCHANT_CONTEXT, packageName) //Must be passed to receive the API response
      intent.putExtra(
        METHOD_NAME,
        METHOD_INIT
      )
      intent.putExtra(ACTIVITY_NAME, this.localClassName) //Must be passed to receive API response
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      startActivity(intent)
    }
  }

  private fun clearTextFields() {
    initRequest.text = ""
    initResponse.text = ""
    idLookupEmail.setText("")
    idLookupRequest.text = ""
    idLookupResponse.text = ""
    initiateValidationResponse.text = ""
    validateOtp.setText("")
    validateRequest.text = ""
    validateResponseText.text = ""
    cardNumberText.setText("")
    expiryDateText.setText("")
    securityCodeText.setText("")
    encryptedCardRequest.text = ""
    encryptedCardResponse.text = ""
    checkoutWithNewCardRequest.text = ""
    checkoutWithNewCardResponse.text = ""
  }

  private fun getCards() {
    val intent = Intent(this, WebViewIntegrationActivity::class.java)
    intent.putExtra(API_REQUEST, "{}")
    intent.putExtra(MERCHANT_CONTEXT, packageName)
    intent.putExtra(METHOD_NAME, METHOD_GET_CARDS)
    intent.putExtra(ACTIVITY_NAME, this.localClassName)
    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
    startActivity(intent)
  }

  private fun encryptCard() {
    val gson = Gson()
    val cardData = gson.toJson(getCardData())
    encryptedCardRequest.text = cardData

    val intent = Intent(this, WebViewIntegrationActivity::class.java)
    intent.putExtra(API_REQUEST, cardData)
    intent.putExtra(MERCHANT_CONTEXT, packageName)
    intent.putExtra(METHOD_NAME, METHOD_ENCRYPT_CARD)
    intent.putExtra(ACTIVITY_NAME, this.localClassName)
    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
    startActivity(intent)
  }

  private fun checkoutWithNewCard() {
    val gson = Gson()
    val encryptCardResponse =
      gson.fromJson(encryptedCard, EncryptCardResponse::class.java)
    val checkoutRequest = CheckoutRequest()
    checkoutRequest.encryptedCard = encryptCardResponse.encryptedCard
    checkoutRequest.cardBrand = encryptCardResponse.cardBrand
    val checkoutRequestJson = gson.toJson(checkoutRequest)
    checkoutWithNewCardRequest.text = checkoutRequestJson
    Log.d(TAG, "CheckoutRequest Json: " + checkoutRequestJson)

    val intent = Intent(this, WebViewIntegrationActivity::class.java)
    intent.putExtra(API_REQUEST, checkoutRequestJson)
    intent.putExtra(MERCHANT_CONTEXT, packageName)
    intent.putExtra(METHOD_NAME, METHOD_CHECKOUT_WITH_NEW_CARD)
    intent.putExtra(ACTIVITY_NAME, this.localClassName)
    intent.putExtra(ACTION_SHEET_MODE, actionSheetCB.isChecked)
    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
    startActivity(intent)
  }

  private fun idLookup() {
    val intent = Intent(this, WebViewIntegrationActivity::class.java)
    val idLookupRequest = getIdLookupRequest()
    Log.d(TAG, "IdLookup Request: $idLookupRequest")
    intent.putExtra(API_REQUEST, idLookupRequest)
    intent.putExtra(MERCHANT_CONTEXT, packageName)
    intent.putExtra(METHOD_NAME, METHOD_ID_LOOKUP)
    intent.putExtra(ACTIVITY_NAME, this.localClassName)
    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
    startActivity(intent)
  }

  private fun initiateValidation(channel: String?) {
    val intent = Intent(this, WebViewIntegrationActivity::class.java)
    val initiateValidationRequestValue = InitiateValidationRequest()

    when (channel) {
      "None" -> initiateValidationRequestValue.requestedValidationChannelId = "None"
      "SMS" -> initiateValidationRequestValue.requestedValidationChannelId = "SMS"
      "Email" -> initiateValidationRequestValue.requestedValidationChannelId = "Email"
    }

    val gson = Gson()
    val initiateValidationJson = gson.toJson(initiateValidationRequestValue)

    intent.putExtra(API_REQUEST, initiateValidationJson)
    intent.putExtra(MERCHANT_CONTEXT, packageName)
    intent.putExtra(METHOD_NAME, METHOD_INITIATE_VALIDATION)
    intent.putExtra(ACTIVITY_NAME, this.localClassName)
    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
    startActivity(intent)
  }

  private fun validate() {
    val validateRequestValue = ValidateRequest()
    validateRequestValue.value = validateOtp.text.toString()
    val gson = Gson()
    val validateJson = gson.toJson(validateRequestValue)
    validateRequest.text = validateJson

    val intent = Intent(this, WebViewIntegrationActivity::class.java)
    intent.putExtra(API_REQUEST, validateJson)
    intent.putExtra(MERCHANT_CONTEXT, packageName)
    intent.putExtra(METHOD_NAME, METHOD_VALIDATE)
    intent.putExtra(ACTIVITY_NAME, this.localClassName)
    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
    startActivity(intent)
  }

  private fun checkoutWithCard() {
    val gson = Gson()
    val checkoutRequest = CheckoutRequest()
    checkoutRequest.srcDigitalCardId = srciDigitalCardId
    val checkoutRequestJson = gson.toJson(checkoutRequest)
    checkoutWithCardRequest.text = checkoutRequestJson
    val intent = Intent(this, WebViewIntegrationActivity::class.java)
    intent.putExtra(API_REQUEST, checkoutRequestJson)
    intent.putExtra(MERCHANT_CONTEXT, packageName)
    intent.putExtra(METHOD_NAME, METHOD_CHECKOUT_WITH_CARD)
    intent.putExtra(ACTIVITY_NAME, this.localClassName)
    intent.putExtra(ACTION_SHEET_MODE, actionSheetCB.isChecked)
    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
    startActivity(intent)
  }

  fun getJsonDataFromAsset(
    context: Context,
    fileName: String
  ): String? {
    val jsonString: String
    try {
      jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
      ioException.printStackTrace()
      return null
    }
    return jsonString
  }

  private fun showProgressDialog() {
    ProgressDialogFragment.showProgressDialog(supportFragmentManager)
  }

  private fun hideProgressDialog() {
    ProgressDialogFragment.dismissProgressDialog()
  }

  private fun getCardData(): EncryptCardRequest {
    var encryptCardRequest: EncryptCardRequest? = null
    encryptCardRequest = EncryptCardRequest()
    encryptCardRequest.cardNumber = cardNumberText.text.toString()
    val expiryMonth = expiryDateText.text
      .toString()
      .substring(EXPIRY_MONTH_START, EXPIRY_MONTH_END)
    encryptCardRequest.panExpirationMonth = expiryMonth
    val expiryYear = expiryDateText.text
      .toString()
      .substring(EXPIRY_YEAR_START, EXPIRY_YEAR_END)
    encryptCardRequest.panExpirationYear = expiryYear
    encryptCardRequest.cardSecurityCode = securityCodeText.text.toString()

    encryptCardRequest.cardholderFirstName = CARD_HOLDER_FIRST_NAME
    encryptCardRequest.cardholderLastName = CARD_HOLDER_LAST_NAME
    val billingAddress =
      MaskedBillingAddress(ZIP, CITY, COUNTRY_CODE, NAME, STATE, LINE3, LINE2, LINE1, null)
    encryptCardRequest.billingAddress = billingAddress

    return encryptCardRequest
  }

  private fun addTextChangeListenerForCardExpiry() {
    expiryDateText.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(
        s: CharSequence,
        start: Int,
        count: Int,
        after: Int
      ) {
        var expiryText = expiryDateText.text.toString()
        val isBackspace = after == 0
        if (isBackspace
          && expiryText.lastIndexOf('/') == expiryText.length - 1
          && expiryText.length == 3
        ) {
          expiryText = expiryText.substring(0, expiryText.length - 2)
          expiryDateText.setText(expiryText)
          expiryDateText.setSelection(expiryDateText.length())
        }
      }

      override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
      ) {
        if (start == 2 && count == 1 && before == 0) {
          var expiryText = expiryDateText.text.toString()
          val lastEntered = expiryText.substring(expiryText.length - 1)
          if (lastEntered != "/") {
            expiryText =
              expiryText.substring(0, expiryText.length - 1) + "/" + lastEntered
          }
          expiryDateText.setText(expiryText)
          expiryDateText.setSelection(expiryDateText.length())
        }
      }

      override fun afterTextChanged(editable: Editable) {
        //Not required for implementation
      }
    })
  }

  private fun processGetCards(response: String) {
    val gson = Gson()
    val type: Type = object : TypeToken<List<MaskedCardsItem>>() {}.type

    val cardList: List<MaskedCardsItem> = gson.fromJson(response, type)
    if (cardList.isNotEmpty()) {
      setCardList(cardList)
    }
  }

  fun setCardList(validationResponse: List<MaskedCardsItem>) {
    val adapter = getPicassoBuilder()?.let { CardAdapter(this, this, validationResponse, it) }
    recyclerView.adapter = adapter
    val dividerItemDecoration: DividerItemDecorator? =
      ContextCompat.getDrawable(this, drawable.cart_page_list_item_layer_divider)?.let {
        DividerItemDecorator(
          it
        )
      }
    if (dividerItemDecoration != null) {
      recyclerView.addItemDecoration(dividerItemDecoration)
    }
  }

  private fun getIdLookupRequest(): String {
    val gson = Gson()
    val idLookupRequestEmail = IdLookupRequest()
    idLookupRequestEmail.email = idLookupEmail.text.toString()
    idLookupRequest.text = gson.toJson(idLookupRequestEmail)
    return gson.toJson(idLookupRequestEmail)
  }

  private fun getPicassoBuilder(): Picasso? {
    val picassoClient: OkHttpClient = CommunicatorSSLContextFactory().okHttpClient!!
    return Builder(this).downloader(OkHttp3Downloader(picassoClient))
      .build()
  }

  override fun setSrcDigitalCardId(srcDigitalCardId: String?) {
    if (srcDigitalCardId != null) {
      srciDigitalCardId = srcDigitalCardId
    }
  }

  override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    //Not required for implementation
  }

  override fun onNothingSelected(parent: AdapterView<*>?) {
    //Not required for implementation
  }

  private fun showMessageDialog(message: String) {
    val dialogBuilder = AlertDialog.Builder(this)

    // set message of alert dialog
    dialogBuilder.setMessage(message)
      // if the dialog is cancelable
      .setCancelable(false)
      // positive button text and action
      .setNegativeButton(OK) { dialog, _ ->
        dialog.cancel()
      }

    // create dialog box
    val alert = dialogBuilder.create()
    // set title for alert dialog box
    alert.setTitle(ALERT_DIALOG)
    // show alert dialog
    alert.show()
  }

  private fun isValidEmail(email: String): Boolean {
    val pattern: Pattern = Patterns.EMAIL_ADDRESS
    return pattern.matcher(email).matches()
  }

  companion object {
    private val TAG = MainActivity::class.java.simpleName
    private const val MESSAGE = "Please select at least one network"
    private const val INIT_ERROR_MESSAGE = "Please provide srcDpaId"
    private const val OK = "OK"
    private const val ALERT_DIALOG = "Alert Dialog"

    private const val EXPIRY_MONTH_START = 0
    private const val EXPIRY_MONTH_END = 2
    private const val EXPIRY_YEAR_START = 3
    private const val EXPIRY_YEAR_END = 5
    private const val OTP_LENGTH = 6

    private const val CARD_HOLDER_FIRST_NAME = "Jane"
    private const val CARD_HOLDER_LAST_NAME = "Doe"
    private const val NAME = "Jane Doe"
    private const val LINE1 = "150 Fifth Ave "
    private const val LINE2 = "string"
    private const val LINE3 = "string"
    private const val CITY = "New York"
    private const val STATE = "NY"
    private const val ZIP = "10011"
    private const val COUNTRY_CODE = "US"
  }
}
