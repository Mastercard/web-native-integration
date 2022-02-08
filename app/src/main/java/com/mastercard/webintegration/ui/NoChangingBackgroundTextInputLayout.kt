package com.mastercard.webintegration.ui
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

import android.content.Context
import android.graphics.ColorFilter
import android.util.AttributeSet
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.material.textfield.TextInputLayout

/**
 * Custom TextInputLayout with no background change when setError message instead of coloring the
 * textbox background red.
 */
class NoChangingBackgroundTextInputLayout : TextInputLayout {
  constructor(context: Context?) : super(context!!)
  constructor(context: Context?, attrs: AttributeSet?) : super(
    context!!, attrs
  )

  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
    context!!, attrs, defStyleAttr
  )

  override fun setError(error: CharSequence?) {
    super.setError(error)
    val defaultColorFilter = backgroundDefaultColorFilter
    updateBackgroundColorFilter(defaultColorFilter)
  }

  override fun drawableStateChanged() {
    super.drawableStateChanged()
    val defaultColorFilter = backgroundDefaultColorFilter
    updateBackgroundColorFilter(defaultColorFilter)
  }

  private fun updateBackgroundColorFilter(colorFilter: ColorFilter?) {
    if (editText != null && editText!!.background != null) {
      editText!!.background.colorFilter = colorFilter
    }
  }

  private val backgroundDefaultColorFilter: ColorFilter?
    private get() {
      var defaultColorFilter: ColorFilter? = null
      if (editText != null && editText!!.background != null) {
        defaultColorFilter = DrawableCompat.getColorFilter(editText!!.background)
      }
      return defaultColorFilter
    }
}