package com.sportpassword.bm.Views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.sportpassword.bm.R
import com.sportpassword.bm.extensions.dpToPx
import org.jetbrains.anko.image
import tw.com.bluemobile.hbc.utilities.getDrawable
import tw.com.bluemobile.hbc.utilities.getResourceID

class IconWithBGCircle @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
    RelativeLayout(context, attrs, defStyleAttr) {

    val view: View = View.inflate(context, R.layout.icon_with_bg_circle, this)

    var iconIV: ImageView? = null
    var containerLL: LinearLayout? = null

    var iconStr: String = "noPhoto"
    var delegate: IconWithBGCircleDelegate? = null

    init {

        view.findViewById<LinearLayout>(R.id.containerLL) ?. let {
            containerLL = it
        }

        view.findViewById<ImageView>(R.id.iconIV) ?. let {
            iconIV = it
        }
        attrs ?. let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.IconWithBGCircle, 0, 0)

            if (typedArray.hasValue(R.styleable.IconWithBGCircle_iconWithBGCircleIcon)) {

                typedArray.getString(R.styleable.IconWithBGCircle_iconWithBGCircleIcon) ?. let { it1 ->
                    setIcon(it1)
                }
            }

            if (typedArray.hasValue(R.styleable.IconWithBGCircle_iconWithBGCircleFrameWidth)) {
                val value: Int = typedArray.getInt(R.styleable.IconWithBGCircle_iconWithBGCircleFrameWidth, 48)
                val p: LinearLayout.LayoutParams = LinearLayout.LayoutParams(value.dpToPx(context), value.dpToPx(context))
                containerLL?.layoutParams = p
            }

            if (typedArray.hasValue(R.styleable.IconWithBGCircle_iconWithBGCircleIconWidth)) {
                val value: Int = typedArray.getInt(R.styleable.IconWithBGCircle_iconWithBGCircleIconWidth, 24)
                val p: LinearLayout.LayoutParams = LinearLayout.LayoutParams(value.dpToPx(context), value.dpToPx(context))
                iconIV?.layoutParams = p
            }
        }

        this.setOnClickListener {
            delegate?.iconPressed(iconStr)
        }
    }

    fun setIcon(icon: String) {
        this.iconStr = icon
        val res: Int = getResourceID(context, icon, "drawable")
        iconIV?.image = getDrawable(context, res)
    }
}

interface IconWithBGCircleDelegate {
    fun iconPressed(icon: String)
}