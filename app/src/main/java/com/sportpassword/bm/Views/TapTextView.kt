package com.sportpassword.bm.Views

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.then
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.custom.style
import org.jetbrains.anko.textColor
import tw.com.bluemobile.hbc.utilities.getColor
import tw.com.bluemobile.hbc.utilities.getDrawable

class TapTextView@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
    LinearLayout(context, attrs, defStyleAttr) {

    val view: View = View.inflate(context, R.layout.tap_text_view, this)

    var tapTV: TextView? = null
    var isOn: Boolean = false
    var tag: Int = 0

    init {
        attrs ?. let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.TapTextView, 0, 0)

            if (typedArray.hasValue(R.styleable.TapTextView_tapTextViewOnOff)) {
                val toggle: String = typedArray.getString(R.styleable.TapTextView_tapTextViewOnOff)!!
                ((toggle == "on") then { this.on() }) ?: this.off()
            }

            if (typedArray.hasValue(R.styleable.TapTextView_tapTextViewTag)) {
                tag = typedArray.getInt(R.styleable.TapTextView_tapTextViewTag, 0)
            }
        }


        view.findViewById<TextView>(R.id.tapTV) ?. let {
            tapTV = it
        }
    }

    fun on() {
        tapTV?.background = getDrawable(context, R.drawable.button_rc)
//        if (Build.VERSION.SDK_INT < 23) {
//            tapTV?.setTextAppearance(context, R.style.list_text_general)
//        } else {
//            tapTV?.setTextAppearance(R.style.list_text_general)
//        }
        tapTV?.setTextColor(getColor(context, R.color.MY_BLACK))

        this.isOn = true
    }

    fun off() {
        tapTV?.backgroundColor = Color.TRANSPARENT
//        if (Build.VERSION.SDK_INT < 23) {
//            tapTV?.setTextAppearance(context, R.style.list_text_small)
//        } else {
//            tapTV?.setTextAppearance(R.style.list_text_small)
//        }
        tapTV?.setTextColor(getColor(context, R.color.MY_WHITE))

        this.isOn = false
    }

    fun setText(value: String) {
        tapTV?.text = value
    }

    fun setOnThisClickListener(lambda: (Int)-> Unit) {
        this.setOnClickListener {
            this.isOn = !this.isOn
            ((this.isOn) then { this.on() }) ?: this.off()

            lambda(tag)
        }
    }
}