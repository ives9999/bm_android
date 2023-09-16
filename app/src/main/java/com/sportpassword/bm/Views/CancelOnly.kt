package com.sportpassword.bm.Views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.sportpassword.bm.R

class CancelOnly @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
    RelativeLayout(context, attrs, defStyleAttr) {

    val view: View = View.inflate(context, R.layout.cancel_only, this)
    var delegate: CancelOnlyDelegate? = null

    var cancelTV: TextView? = null

    init {
        view.findViewById<TextView>(R.id.cancelTV)?.let {
            cancelTV = it
            setOnClickListener()
        }

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CancelOnly, 0, 0)

            if (typedArray.hasValue(R.styleable.CancelOnly_cancelOnlyTV)) {
                setText(typedArray.getString(R.styleable.CancelOnly_cancelOnlyTV)!!)
            }
        }
    }

    fun setOnClickListener() {
        cancelTV ?. let {
            it.setOnClickListener {
                delegate?.cancel2()
            }
        }
    }

    fun setText(text: String) {
        cancelTV?.text = text
    }
}

interface CancelOnlyDelegate {
    fun cancel2()
}