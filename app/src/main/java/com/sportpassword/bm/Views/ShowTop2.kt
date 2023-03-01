package com.sportpassword.bm.Views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.sportpassword.bm.Controllers.BaseActivity
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.dpToPx
import com.sportpassword.bm.Utilities.then
import org.jetbrains.anko.backgroundColor

class ShowTop2 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
    RelativeLayout(context, attrs, defStyleAttr) {

    val view = View.inflate(context, R.layout.show_top2, this)

    private var rootRL: RelativeLayout? = null
    private var prevIB: ImageButton? = null
    private var editIB: ImageButton? = null
    private var cartIB: ImageButton? = null
    private var searchIB: ImageButton? = null
    private var titleTV: TextView? = null

    private var refreshIcon: IconView2? = null
    private var addIconText: IconText2? = null

    var delegate: ShowTop2Delegate? = null

    init {
        (context as? BaseActivity) ?. let { delegate->
            view.findViewById<ImageButton>(R.id.prevIB) ?. let {
                prevIB = it
                it.setOnClickListener {
                    delegate.prev()
                }
            }

            view.findViewById<RelativeLayout>(R.id.rootRL) ?. let {
                rootRL = it
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

    fun showRefresh() {

        refreshIcon = IconView2(context)
        refreshIcon!!.setIcon("ic_refresh_svg")

        val lp: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        lp.addRule(RelativeLayout.ALIGN_PARENT_END)
        lp.addRule(RelativeLayout.CENTER_VERTICAL)
        lp.marginEnd = 20.toInt().dpToPx(context)

        rootRL?.addView(refreshIcon, lp)

        refreshIcon!!.setOnClickListener {
            delegate?.showTop2Refresh()
        }
    }

    fun showAdd(marginEnd: Int) {
        addIconText = IconText2(context)
        addIconText!!.setIcon("ic_add_svg")
        addIconText!!.setText("新增")

        val lp: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
//        if (refreshIcon != null) {
//            lp.addRule(RelativeLayout.LEFT_OF, refreshIcon!!.id)
//        }
        lp.addRule(RelativeLayout.ALIGN_PARENT_END)
        lp.addRule(RelativeLayout.CENTER_VERTICAL)
        lp.marginEnd = marginEnd.dpToPx(context)

        rootRL?.addView(addIconText, lp)

        addIconText!!.setOnClickListener {
            delegate?.showTop2Add()
        }
    }
}

interface ShowTop2Delegate {
    fun showTop2Refresh()
    fun showTop2Add()
}
