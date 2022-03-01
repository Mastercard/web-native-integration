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

package com.mastercard.webintegration.validate.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mastercard.webintegration.R
import com.mastercard.webintegration.utils.DisplayView
import com.mastercard.webintegration.validate.data.MaskedCardsItem
import com.squareup.picasso.Picasso

class CardAdapter(
  private val context: Context,
  private val view: DisplayView,
  private val cardList: List<MaskedCardsItem>?,
  private val picasso: Picasso
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  private var selectedPos = RecyclerView.NO_POSITION
  override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val view = LayoutInflater.from(viewGroup.context)
      .inflate(R.layout.card_list_item, viewGroup, false)
    return CardViewHolder(view)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val cardViewHolder = holder as CardViewHolder
    val cardItem = getItem(position)
    cardViewHolder.cardNameTextView.text = cardItem.digitalCardData!!.descriptorName
    cardViewHolder.cardNumberTextView.text = cardViewHolder.cardNumberTextView.context
      .getString(R.string.masked_last_four_digit, cardItem.panLastFour)
    if (cardItem.panExpirationMonth != null && cardItem.panExpirationYear != null) {
      cardViewHolder.cardExpiryTextView.visibility = View.VISIBLE
      val cardExpiryString =
        cardItem.panExpirationMonth + EXPIRY_CONCAT + cardItem.panExpirationYear
      cardViewHolder.cardExpiryTextView.text = cardExpiryString
    } else {
      cardViewHolder.cardExpiryTextView.visibility = View.GONE
    }
    loadImages(cardItem.digitalCardData.artUri, cardViewHolder.cardImageView)
    cardViewHolder.itemView.isSelected = selectedPos == position
    cardViewHolder.cardItemLayout.setOnClickListener {
      cardViewHolder.cardItemLayout.requestFocusFromTouch()
      view.setSrcDigitalCardId(cardItem.srcDigitalCardId)
      notifyItemChanged(selectedPos)
      selectedPos = cardViewHolder.layoutPosition
      notifyItemChanged(selectedPos)
    }
  }

  override fun getItemCount(): Int {
    return cardList?.size ?: 0
  }

  private fun getItem(position: Int): MaskedCardsItem {
    return cardList!![position]
  }

  private fun loadImages(url: String?, productImageView: ImageView) {
    if (url != null && !url.isEmpty()) {
      picasso.isLoggingEnabled = true
      picasso.load(url)
        .placeholder(R.drawable.cart_item_default_image)
        .error(R.drawable.cart_item_default_image)
        .into(productImageView)
    }
  }

  internal inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val cardItemLayout: ConstraintLayout
    val cardImageView: ImageView
    val cardNameTextView: TextView
    val cardNumberTextView: TextView
    val cardExpiryTextView: TextView

    init {
      cardImageView = itemView.findViewById(R.id.card_image)
      cardNameTextView = itemView.findViewById(R.id.card_name)
      cardNumberTextView = itemView.findViewById(R.id.card_number)
      cardItemLayout = itemView.findViewById(R.id.card_item_layout)
      cardExpiryTextView = itemView.findViewById(R.id.card_expiry)
    }
  }

  companion object {
    private const val EXPIRY_CONCAT = "/"
  }
}