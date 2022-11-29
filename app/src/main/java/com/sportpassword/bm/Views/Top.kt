package com.sportpassword.bm.Views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import com.sportpassword.bm.Controllers.BaseActivity
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.then

class Top @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
    RelativeLayout(context, attrs, defStyleAttr) {

    val view = View.inflate(context, R.layout.top_view, this)
    private var prevIB: ImageButton? = null
//    private var addIB: ImageButton = view.findViewById(R.id.addIcon)
//    private var titleTV: TextView = view.findViewById(R.id.topTitleLbl)

    init {
        (context as? BaseActivity) ?. let { delegate->
            view.findViewById<ImageButton>(R.id.prevIcon) ?. let {
                prevIB = it
                it.setOnClickListener {
                    delegate.prev()
                }
            }

//            addIB.setOnClickListener {
//                delegate.addPressed(it)
//            }
        }
    }

//    fun showAdd(isShow: Boolean = false) {
//        addIB.visibility = (isShow then { VISIBLE }) ?: GONE
//    }
//
//    fun showPrev(isShow: Boolean = true) {
//        prevIB.visibility = (isShow then { VISIBLE }) ?: GONE
//    }
//
//    fun setTitle(title: String) {
//        titleTV.text = title
//    }
}