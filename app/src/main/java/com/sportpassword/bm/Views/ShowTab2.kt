package com.sportpassword.bm.Views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.isInt
import org.jetbrains.anko.textColor
import tw.com.bluemobile.hbc.utilities.getColor
import tw.com.bluemobile.hbc.utilities.getDrawable
import tw.com.bluemobile.hbc.utilities.getResourceID
import tw.com.bluemobile.hbc.utilities.then

class ShowTab2@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
    LinearLayout(context, attrs, defStyleAttr) {

    val view: View = View.inflate(context, R.layout.show_tab2, this)

    var delegate: ShowTab2Delegate? = null

    var LL1: LinearLayout? = null
    var LL2: LinearLayout? = null
    var LL3: LinearLayout? = null

    var TV1: TextView? = null
    var TV2: TextView? = null
    var TV3: TextView? = null

    var views: ArrayList<LinearLayout> = arrayListOf()

    var onIdx: Int = 0

    init {

        view.findViewById<LinearLayout>(R.id.LL1) ?. let {
            it.tag = 0
            LL1 = it
            views.add(it)
        }

        view.findViewById<LinearLayout>(R.id.LL2) ?. let {
            it.tag = 1
            LL2 = it
            views.add(it)
        }

        view.findViewById<LinearLayout>(R.id.LL3) ?. let {
            it.tag = 2
            LL3 = it
            views.add(it)
        }

        view.findViewById<TextView>(R.id.TV1) ?. let {
            TV1 = it
        }

        view.findViewById<TextView>(R.id.TV2) ?. let {
            TV2 = it
        }

        view.findViewById<TextView>(R.id.TV3) ?. let {
            TV3 = it
        }

        if (LL1 != null) {
            on(this.LL1!!)
        }

        attrs ?. let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.ShowTab2, 0, 0)

            if (typedArray.hasValue(R.styleable.ShowTab2_showTab2TV1)) {
                TV1?.text = typedArray.getString(R.styleable.ShowTab2_showTab2TV1)
            }

            if (typedArray.hasValue(R.styleable.ShowTab2_showTab2TV2)) {
                TV2?.text = typedArray.getString(R.styleable.ShowTab2_showTab2TV2)
            }

            if (typedArray.hasValue(R.styleable.ShowTab2_showTab2TV3)) {
                TV3?.text = typedArray.getString(R.styleable.ShowTab2_showTab2TV3)
            }
        }

    }

    fun on(linearLayout: LinearLayout) {

        if (linearLayout.tag != null && linearLayout.tag.toString().isInt()) {
            val tag: Int = linearLayout.tag.toString().toInt()
            val str: String = "show_tab2_view${tag + 1}_on"
            val resource: Int = getResourceID(context, str, "drawable")
            //val resource: Int = resources.getIdentifier(str, "drawable", context.packageName)
            linearLayout.background = getDrawable(context, resource)
            //linearLayout.background = getDrawable(context, R.drawable.show_tab2_view1_on)
        }

        val a = linearLayout.getChildAt(0) as? TextView
        a?.textColor = getColor(context, R.color.MY_BLACK)
    }

    fun off(linearLayout: LinearLayout) {

        if (linearLayout.tag != null && linearLayout.tag.toString().isInt()) {
            val tag: Int = linearLayout.tag.toString().toInt()
            val str: String = "show_tab2_view${tag + 1}_off"
            val resource: Int = getResourceID(context, str, "drawable")
            linearLayout.background = getDrawable(context, resource)
        }

        val a = linearLayout.getChildAt(0) as? TextView
        a?.textColor = getColor(context, R.color.MY_WHITE)
    }

    fun setOnClickListener() {

        LL1?.setOnClickListener {
            toggle(it)
//            onIdx = 0
//            if (onIdx != oldIdx) {
//                delegate?.tabPressed(onIdx)
//            }
        }

        LL2?.setOnClickListener {
            toggle(it)
//            onIdx = 1
//            if (onIdx != oldIdx) {
//                delegate?.tabPressed(1)
//            }
        }

        LL3?.setOnClickListener {
            toggle(it)
//            onIdx = 2
//            if (onIdx != oldIdx) {
//                delegate?.tabPressed(2)
//            }
        }
    }

    private fun toggle(sender: View) {

        if (sender.tag != null && sender.tag.toString().isInt()) {
            val oldIdx: Int = onIdx
            onIdx = sender.tag.toString().toInt()
            //按下不同的選項才執行
            if (onIdx != oldIdx) {
                delegate?.tabPressed(onIdx)

                for (view in views) {
                    ((view == sender) then { on(view) }) ?: off(view)
                }
            }
        }
    }
}

interface ShowTab2Delegate {
    fun tabPressed(idx: Int)
}