package com.sportpassword.bm.Views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.dpToPx

class ShowButton2@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
    LinearLayout(context, attrs, defStyleAttr) {

    val view: View = View.inflate(context, R.layout.show_button2, this)
    var containerLL: LinearLayout? = null
    var TV: TextView? = null

    var idx: Int = 0
    var delegate: ShowButton2Delegate? = null

    init {

        view.findViewById<LinearLayout>(R.id.containerLL) ?. let {
            containerLL = it
        }

        view.findViewById<TextView>(R.id.TV) ?. let {
            TV = it
        }

        attrs ?. let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.ShowButton2, 0, 0)

            if (typedArray.hasValue(R.styleable.ShowButton2_showButtonTV)) {
                TV?.text = typedArray.getString(R.styleable.ShowButton2_showButtonTV)
            }

            if (typedArray.hasValue(R.styleable.ShowButton2_showButtonWidth)) {
                val width: Int = typedArray.getInt(R.styleable.ShowButton2_showButtonWidth, 160)
                if (containerLL != null) {
                    val p: LayoutParams = containerLL!!.layoutParams as LayoutParams
                    p.width = width.dpToPx(context)
                    containerLL!!.layoutParams = p
                }
            }

            if (typedArray.hasValue(R.styleable.ShowButton2_showButtonHeight)) {
                val height: Int = typedArray.getInt(R.styleable.ShowButton2_showButtonHeight, 40)
                if (containerLL != null) {
                    val p: LayoutParams = containerLL!!.layoutParams as LayoutParams
                    p.height = height.dpToPx(context)
                    containerLL!!.layoutParams = p
                }
            }
        }

        this.setOnClickListener {
            delegate?.pressed(idx)
        }
    }
}

interface ShowButton2Delegate {
    fun pressed(idx: Int)
}





















