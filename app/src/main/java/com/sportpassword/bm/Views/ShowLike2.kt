package com.sportpassword.bm.Views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.sportpassword.bm.R

class ShowLike2@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
    LinearLayout(context, attrs, defStyleAttr) {

    val view: View = View.inflate(context, R.layout.show_like2, this)

}