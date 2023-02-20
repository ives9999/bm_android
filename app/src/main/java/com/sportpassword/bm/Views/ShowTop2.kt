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

class ShowTop2 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
    RelativeLayout(context, attrs, defStyleAttr) {

    val view = View.inflate(context, R.layout.show_top2, this)
    private var prevIB: ImageButton? = null
    private var editIB: ImageButton? = null
    private var cartIB: ImageButton? = null
    private var searchIB: ImageButton? = null
    private var titleTV: TextView? = null

    init {
        (context as? BaseActivity) ?. let { delegate->
            view.findViewById<ImageButton>(R.id.prevIB) ?. let {
                prevIB = it
                it.setOnClickListener {
                    delegate.prev()
                }
            }

            view.findViewById<ImageButton>(R.id.editIB) ?. let {
                editIB = it
            }

            view.findViewById<ImageButton>(R.id.cartIB) ?. let {
                cartIB = it
            }

            view.findViewById<ImageButton>(R.id.searchIB) ?. let {
                searchIB = it
            }

            view.findViewById<TextView>(R.id.titleTV) ?. let {
                titleTV = it
            }
        }
    }

    fun showEdit(isShow: Boolean = false) {
        editIB?.visibility = (isShow then { VISIBLE }) ?: GONE
    }

    fun showPrev(isShow: Boolean = true) {
        prevIB?.visibility = (isShow then { VISIBLE }) ?: GONE
    }

    fun showCart(isShow: Boolean = true) {
        cartIB?.visibility = (isShow then { VISIBLE }) ?: GONE
    }

    fun showSearch(isShow: Boolean = true) {
        searchIB?.visibility = (isShow then { VISIBLE }) ?: GONE
    }

    fun setTitle(title: String) {
        titleTV?.text = title
    }
}