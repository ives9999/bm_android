package com.sportpassword.bm.bm_new.ui.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class LinearItemDecoration(
    val top: Int = 0,
    val bottom: Int = 0,
    val start: Int = 0,
    val end: Int = 0,
    var spacing: Int = 0,
    var isVertical: Boolean = true,
    var spacingStartIndex: Int = 0
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        if (isVertical) {
            if (position == spacingStartIndex) {
                outRect.top = top
                outRect.bottom = spacing
            } else if (position == (parent.adapter?.itemCount?.minus(1) ?: return)) {
                outRect.top = 0
                outRect.bottom = bottom
            } else {
                outRect.top = 0
                outRect.bottom = spacing
            }
            if (position < spacingStartIndex) {
                outRect.bottom = 0
            }
            outRect.right = end
            outRect.left = start
        } else {
            if (position == spacingStartIndex) {
                outRect.left = start
                outRect.right = spacing
            } else if (position == (parent.adapter?.itemCount?.minus(1) ?: return)) {
                outRect.left = 0
                outRect.right = end
            } else {
                outRect.left = 0
                outRect.right = spacing
            }
            if (position < spacingStartIndex) {
                outRect.right = 0
            }
            outRect.top = top
            outRect.bottom = bottom
        }
    }
}