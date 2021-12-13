package com.mastercard.webintegration.models

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import kotlin.Throws
import okhttp3.OkHttpClient
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView

/**
 * This class shows custom divider in Cart Page list.
 */
class DividerItemDecorator(private val mDivider: Drawable) : ItemDecoration() {
    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val dividerLeft = parent.paddingLeft
        val dividerRight = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0..childCount - 1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val dividerTop = child.bottom + params.bottomMargin
            val dividerBottom = dividerTop + mDivider.intrinsicHeight
            mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
            mDivider.draw(canvas)
        }
    }
}