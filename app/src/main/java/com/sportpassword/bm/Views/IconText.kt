package com.sportpassword.bm.Views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.sportpassword.bm.R
import org.jetbrains.anko.image
import tw.com.bluemobile.hbc.utilities.getDrawable
import tw.com.bluemobile.hbc.utilities.getResourceID

class IconText @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
    RelativeLayout(context, attrs, defStyleAttr) {

    val view: View = View.inflate(context, R.layout.icon_text, this)

    private var rootLL: LinearLayout? = null
    private var iconIV: ImageView? = null
    private var textTV: TextView? = null

    var iconStr: String = "noPhoto"
    var delegate: IconText2Delegate? = null

    init {
        view.findViewById<LinearLayout>(R.id.rootLL) ?. let {
            rootLL = it
        }

        view.findViewById<ImageView>(R.id.iconIV) ?. let {
            iconIV = it
        }

        view.findViewById<TextView>(R.id.textTV) ?. let {
            textTV = it
        }

        attrs ?. let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.IconText, 0, 0)

            if (typedArray.hasValue(R.styleable.IconText_iconTextIcon)) {
                typedArray.getString(R.styleable.IconText_iconTextIcon) ?. let { it1 ->
                    setIcon(it1)
                }
            }

            if (typedArray.hasValue(R.styleable.IconText_iconTextText)) {
                typedArray.getString(R.styleable.IconText_iconTextText) ?. let { it1 ->
                    setText(it1)
                }
            }
        }

        this.setOnThisClickListener()
    }

    fun setIcon(icon: String) {
        this.iconStr = icon
        val res: Int = getResourceID(context, icon, "drawable")
        iconIV?.image = getDrawable(context, res)
    }

    fun setText(text: String) {
        this.textTV?.text = text
    }

    fun setOnThisClickListener() {
        this.setOnClickListener {
            delegate?.iconText2Pressed(iconStr)
        }
    }
}

interface IconTextDelegate {
    fun iconTextPressed(iconStr: String)
}