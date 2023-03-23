package com.sportpassword.bm.Views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import com.sportpassword.bm.R

class MainEditText2 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
    RelativeLayout(context, attrs, defStyleAttr) {

    val view: View = View.inflate(context, R.layout.main_edit_text2, this)

    private var titleTV: TextView? = null
    private var contentET: EditText? = null

    init {
        view.findViewById<TextView>(R.id.titleTV) ?. let {
            titleTV = it
        }

        view.findViewById<EditText>(R.id.contentET) ?. let {
            contentET = it
        }

        attrs ?. let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.MainEditText2, 0, 0)

        }
    }
}