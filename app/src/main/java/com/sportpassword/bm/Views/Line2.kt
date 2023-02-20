package com.sportpassword.bm.Views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.sportpassword.bm.R

class Line2@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
    LinearLayout(context, attrs, defStyleAttr) {

    val view: View = View.inflate(context, R.layout.line2, this)
    init {
        attrs ?. let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.line, 0, 0)
        }
    }
}