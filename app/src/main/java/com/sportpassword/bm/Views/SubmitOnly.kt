package com.sportpassword.bm.Views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.sportpassword.bm.R

class SubmitOnly @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
    RelativeLayout(context, attrs, defStyleAttr) {

    val view: View = View.inflate(context, R.layout.submit_only, this)
    var delegate: SubmitDelegate? = null

    var submitTV: TextView? = null

    init {
        view.findViewById<TextView>(R.id.submitTV)?.let {
            submitTV = it
            setOnClickListener()
        }

        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.SubmitOnly, 0, 0)

            if (typedArray.hasValue(R.styleable.SubmitOnly_submitOnlyTV)) {
                setText(typedArray.getString(R.styleable.SubmitOnly_submitOnlyTV)!!)
            }
        }
    }

    fun setOnClickListener() {
        submitTV ?. let {
            it.setOnClickListener {
                delegate?.submit2()
            }
        }
    }

    fun setText(text: String) {
        submitTV?.text = text
    }
}

interface SubmitDelegate {
    fun submit2()
}