package com.sportpassword.bm.Views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.sportpassword.bm.R
import org.jetbrains.anko.image
import com.sportpassword.bm.functions.getDrawable
import com.sportpassword.bm.functions.getResourceID

class IconView2@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
    LinearLayout(context, attrs, defStyleAttr) {

    val view: View = View.inflate(context, R.layout.icon_view2, this)

    var iconIV: ImageView? = null
    var containerLL: LinearLayout? = null

    var iconStr: String = "noPhoto"
    var delegate: IconView2Delegate? = null

    init {

        view.findViewById<LinearLayout>(R.id.containerLL) ?. let {
            containerLL = it
        }

        view.findViewById<ImageView>(R.id.iconIV) ?. let {
            iconIV = it
        }
        attrs ?. let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.IconView2, 0, 0)

            if (typedArray.hasValue(R.styleable.IconView2_iconView2Icon)) {

                typedArray.getString(R.styleable.IconView2_iconView2Icon) ?. let { it1 ->
                    setIcon(it1)
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