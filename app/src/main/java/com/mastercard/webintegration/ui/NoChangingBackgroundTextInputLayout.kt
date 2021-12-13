package com.mastercard.webintegration.ui

import android.content.Context
import com.google.android.material.textfield.TextInputLayout
import android.graphics.ColorFilter
import androidx.core.graphics.drawable.DrawableCompat
import android.os.Bundle
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.widget.ProgressBar

/**
 * Custom TextInputLayout with no background change when setError message instead of coloring the
 * textbox background red.
 */
class NoChangingBackgroundTextInputLayout : TextInputLayout {
    constructor(context: Context?) : super(context!!) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
    }

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