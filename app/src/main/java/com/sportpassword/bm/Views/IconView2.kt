package com.sportpassword.bm.Views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.sportpassword.bm.R
import org.jetbrains.anko.image
import tw.com.bluemobile.hbc.utilities.getDrawable
import tw.com.bluemobile.hbc.utilities.getResourceID

class IconView2@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
    LinearLayout(context, attrs, defStyleAttr) {

    val view: View = View.inflate(context, R.layout.icon_view2, this)

    var iconIV: ImageView? = null
    var containerLL: LinearLayout? = null

    var iconStr: String = "noPhont"
    var delegate: IconView2Delegate? = null

    init {

        view.findViewById<LinearLayout>(R.id.containerLL) ?. let {
            containerLL = it
        }

        view.findViewById<ImageView>(R.id.iconIV) ?. let {
            iconIV = it
        }
        attrs ?. let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.IconView, 0, 0)

            if (typedArray.hasValue(R.styleable.IconView_iconViewIcon)) {

                typedArray.getString(R.styleable.IconView_iconViewIcon) ?. let { it1 ->
                    setIcon(it1)
//                    this.icon = it1
//                    val res: Int = getResourceID(context, it1, "drawable")
//                    iconIV?.image = getDrawable(context, res)
                }
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

interface IconView2Delegate {
    fun iconPressed(icon: String)
}