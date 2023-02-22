package com.sportpassword.bm.Views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.sportpassword.bm.R
import com.sportpassword.bm.extensions.toTwoString

class NOTV2@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
    LinearLayout(context, attrs, defStyleAttr) {

    val view: View = View.inflate(context, R.layout.no_tv, this)

    var noTV: TextView? = null
    init {

        view.findViewById<TextView>(R.id.no) ?. let {
            noTV = it
        }

        attrs ?. let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.NOTV2, 0, 0)

            if (typedArray.hasValue(R.styleable.NOTV2_no)) {
                noTV?.text = typedArray.getInt(R.styleable.NOTV2_no, 5).toTwoString()
            }
        }
    }

    fun setNO(value: Int) {
        val n: String = value.toTwoString()
        noTV?.text = n
    }
}