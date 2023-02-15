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

    //代理人，按下每一個tab時，要接收事件的代理人
    var delegate: ShowTab2Delegate? = null

    var LL1: LinearLayout? = null
    var LL2: LinearLayout? = null
    var LL3: LinearLayout? = null

    var TV1: TextView? = null
    var TV2: TextView? = null
    var TV3: TextView? = null

    //存放所有tab view的陣列
    var views: ArrayList<LinearLayout> = arrayListOf()

    //記錄目前按下的事那一個tab
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

        //直接由xml設定tab的標籤文字
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

    //tab被點選時外觀的變化
    fun on(linearLayout: LinearLayout) {

        if (linearLayout.tag != null && linearLayout.tag.toString().isInt()) {
            val tag: Int = linearLayout.tag.toString().toInt()
            val str: String = "show_tab2_view${tag + 1}_on"
            val resource: Int = getResourceID(context, str, "drawable")
            linearLayout.background = getDrawable(context, resource)
        }

        val tv: TextView? = linearLayout.getChildAt(0) as? TextView
        tv?.textColor = getColor(context, R.color.MY_BLACK)
    }

    //tab失去被點選時外觀的變化
    fun off(linearLayout: LinearLayout) {

        if (linearLayout.tag != null && linearLayout.tag.toString().isInt()) {
            val tag: Int = linearLayout.tag.toString().toInt()
            val str: String = "show_tab2_view${tag + 1}_off"
            val resource: Int = getResourceID(context, str, "drawable")
            linearLayout.background = getDrawable(context, resource)
        }

        val tv: TextView? = linearLayout.getChildAt(0) as? TextView
        tv?.textColor = getColor(context, R.color.MY_WHITE)
    }

    //設定每一個tab要執行按下的函數
    fun setOnClickListener() {

        LL1?.setOnClickListener {
            pressed(it)
        }

        LL2?.setOnClickListener {
            pressed(it)
        }

        LL3?.setOnClickListener {
            pressed(it)
        }
    }

    //tab被點選時要執行的動作
    private fun pressed(sender: View) {

        if (sender.tag != null && sender.tag.toString().isInt()) {
            val oldIdx: Int = onIdx
            onIdx = sender.tag.toString().toInt()
            //按下不同的選項才執行
            if (onIdx != oldIdx) {

                for (view in views) {
                    ((view == sender) then { on(view) }) ?: off(view)
                }

                delegate?.tabPressed(onIdx)
            }
        }
    }
}

interface ShowTab2Delegate {
    fun tabPressed(idx: Int)
}