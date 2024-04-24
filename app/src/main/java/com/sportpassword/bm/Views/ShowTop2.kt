package com.sportpassword.bm.Views

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.sportpassword.bm.R
import com.sportpassword.bm.extensions.dpToPx
import com.sportpassword.bm.functions.then

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
    private var logIconText: IconText2? = null

    var delegate: ShowTop2Delegate? = null

    init {
        (context as? AppCompatActivity) ?. let { delegate->
            view.findViewById<ImageButton>(R.id.prevIB) ?. let {
                prevIB = it
                it.setOnClickListener {
                    prev(delegate)
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

    fun showLog(marginEnd: Int) {
        logIconText = IconText2(context)
        logIconText!!.setIcon("ic_log_svg")
        logIconText!!.setText("查詢")

        val lp: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        lp.addRule(RelativeLayout.ALIGN_PARENT_END)
        lp.addRule(RelativeLayout.CENTER_VERTICAL)
        lp.marginEnd = marginEnd.dpToPx(context)

        rootRL?.addView(logIconText, lp)

        logIconText!!.setOnClickListener {
            delegate?.showTop2Log()
        }
    }

    fun prev(activity: AppCompatActivity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm != null && activity.currentFocus != null) {
            imm.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0);
        }

        val intent = Intent()
        activity.setResult(Activity.RESULT_CANCELED, intent)
        activity.finish()
    }
}

interface ShowTop2Delegate {
    fun showTop2Refresh(){}
    fun showTop2Add(){}
    fun showTop2Log(){}
}
