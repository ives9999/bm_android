package com.sportpassword.bm.Views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.setImage
import org.jetbrains.anko.image
import tw.com.bluemobile.hbc.utilities.getDrawable
import tw.com.bluemobile.hbc.utilities.getResourceID

class IconTextVertical2@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
    LinearLayout(context, attrs, defStyleAttr) {

    val view: View = View.inflate(context, R.layout.icon_text_vertical2, this)

    var containerLL: LinearLayout? = null
    var iconIV: ImageView? = null
    var textTV: TextView? = null

    var iconStr: String = "noPhoto"
    var text: String = "100 點"
    var delegate: IconTextVertical2Delegate? = null

    init {

        view.findViewById<LinearLayout>(R.id.containerLL)?.let {
            containerLL = it
        }

        view.findViewById<ImageView>(R.id.iconIV)?.let {
            iconIV = it
        }

        view.findViewById<TextView>(R.id.textTV) ?. let {
            textTV = it
        }

        attrs ?. let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.IconTextVertical2, 0, 0)
            if (typedArray.hasValue(R.styleable.IconText2_iconText2Icon)) {
                typedArray.getString(R.styleable.IconText2_iconText2Icon) ?. let { it1 ->
                    iconStr = it1
                    setIcon(it1)
                }
            }

            if (typedArray.hasValue(R.styleable.IconText2_iconText2Text)) {
                typedArray.getString(R.styleable.IconText2_iconText2Text) ?. let { it1 ->
                    text = it1
                    textTV?.text = it1
                }
            }
        }
    }

    fun setIcon(icon: String) {
        this.iconStr = icon
        val res: Int = getResourceID(context, icon, "drawable")
        if (res > 0) {
            iconIV?.image = getDrawable(context, res)
        }
    }
}

interface IconTextVertical2Delegate {
    fun iconTextPressed()
}