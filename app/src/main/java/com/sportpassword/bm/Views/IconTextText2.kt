package com.sportpassword.bm.Views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.sportpassword.bm.R
import com.sportpassword.bm.extensions.dpToPx
import org.jetbrains.anko.image
import tw.com.bluemobile.hbc.utilities.getDrawable
import tw.com.bluemobile.hbc.utilities.getResourceID

class IconTextText2 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
    RelativeLayout(context, attrs, defStyleAttr) {

    val view: View = View.inflate(context, R.layout.icon_text_text2, this)

    private var rootLL: LinearLayout? = null
    private var iconIV: ImageView? = null
    private var titleTV: TextView? = null
    private var showTV: TextView? = null

    var iconStr: String = "noPhoto"
    var iconWidth: Int = 24
    var iconHeight: Int = 24
    var delegate: IconTextText2Delegate? = null

    init {
        view.findViewById<LinearLayout>(R.id.rootLL) ?. let {
            rootLL = it
        }

        view.findViewById<ImageView>(R.id.iconIV) ?. let {
            iconIV = it
        }

        view.findViewById<TextView>(R.id.titleTV) ?. let {
            titleTV = it
        }

        view.findViewById<TextView>(R.id.showTV) ?. let {
            showTV = it
        }

        attrs ?. let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.IconTextText2, 0, 0)

            if (typedArray.hasValue(R.styleable.IconTextText2_iconTextText2Icon)) {
                typedArray.getString(R.styleable.IconTextText2_iconTextText2Icon) ?. let { it1 ->
                    setIcon(it1)
                }
            }

            if (typedArray.hasValue(R.styleable.IconTextText2_iconTextText2Title)) {
                typedArray.getString(R.styleable.IconTextText2_iconTextText2Title) ?. let { it1 ->
                    setTitle(it1)
                }
            }

            if (typedArray.hasValue(R.styleable.IconTextText2_iconTextText2Show)) {
                typedArray.getString(R.styleable.IconTextText2_iconTextText2Show) ?. let { it1 ->
                    setShow(it1)
                }
            }

            if (typedArray.hasValue(R.styleable.IconTextText2_iconTextText2IconWidth)) {
                iconWidth = typedArray.getInt(R.styleable.IconTextText2_iconTextText2IconWidth, 24)
            }

            if (typedArray.hasValue(R.styleable.IconTextText2_iconTextText2IconHeight)) {
                iconHeight = typedArray.getInt(R.styleable.IconTextText2_iconTextText2IconHeight, 24)
            }

            val p: LinearLayout.LayoutParams = LinearLayout.LayoutParams(iconWidth.dpToPx(context), iconHeight.dpToPx(context))
            iconIV?.layoutParams = p
        }

        this.setOnThisClickListener()
    }

    fun setIcon(icon: String) {
        this.iconStr = icon
        val res: Int = getResourceID(context, icon, "drawable")
        iconIV?.image = getDrawable(context, res)
    }

    fun setTitle(text: String) {
        this.titleTV?.text = text
    }

    fun setShow(text: String) {
        this.showTV?.text = text
    }

    fun setOnThisClickListener() {
        this.setOnClickListener {
            delegate?.iconTextText2Pressed(iconStr)
        }
    }
}

interface IconTextText2Delegate {
    fun iconTextText2Pressed(iconStr: String)
}