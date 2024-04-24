package com.sportpassword.bm.Views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.sportpassword.bm.Controllers.BaseActivity
import com.sportpassword.bm.R
import com.sportpassword.bm.extensions.dpToPx
import com.sportpassword.bm.functions.getColor
import com.sportpassword.bm.functions.getDrawable
import com.sportpassword.bm.functions.getScreenWidth
import com.sportpassword.bm.functions.then

class Bottom2@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
    RelativeLayout(context, attrs, defStyleAttr) {

    val view = View.inflate(context, R.layout.bottom2, this)

    var submitBtn: Button? = null
    private var likeLL: LinearLayout? = null
    private var cancelBtn: Button? = null

    private var likeIconIV: ImageView? = null
    private var likeCountTV: TextView? = null

    var bottom_button_count = 3
    var screenWidth: Int = 0
    val button_width: Int = 120

    var isShowSubmitBtn: Boolean = true
    var isShowLikeBtn: Boolean = true
    var isShowCancelBtn: Boolean = true

    init {
        (context as? BaseActivity) ?. let { delegate ->
            view.findViewById<Button>(R.id.submitBtn) ?.let {
                submitBtn = it
            }

            view.findViewById<Button>(R.id.cancelBtn) ?. let {
                cancelBtn = it
            }

            view.findViewById<LinearLayout>(R.id.likeLL) ?. let {
                likeLL = it
            }

            view.findViewById<ImageView>(R.id.likeIconIV) ?. let {
                likeIconIV = it
            }

            view.findViewById<TextView>(R.id.likeCountTV) ?. let {
                likeCountTV = it
            }
        }

        screenWidth = getScreenWidth(resources)
    }

    fun showButton(isShowSubmit: Boolean = true, isShowLike: Boolean = true, isShowCancel: Boolean = true) {

        this.isShowSubmitBtn = isShowSubmit
        this.isShowLikeBtn = isShowLike
        this.isShowCancelBtn = isShowCancel

        this.bottom_button_count = 0
        if (this.isShowSubmitBtn) { this.bottom_button_count += 1 }
        if (this.isShowLikeBtn) { this.bottom_button_count += 1 }
        if (this.isShowCancelBtn) { this.bottom_button_count += 1 }

        this.submitBtn!!.visibility = ((this.isShowSubmitBtn) then { View.VISIBLE }) ?: View.GONE
        this.cancelBtn!!.visibility = ((this.isShowCancelBtn) then { View.VISIBLE }) ?: View.GONE
        this.likeLL!!.visibility = ((this.isShowLikeBtn) then { View.VISIBLE }) ?: View.GONE

        val padding: Int = getBottomButtonPadding()
        setAnchor(padding)
    }

    fun setOnSubmitClickListener(lambda: ()-> Unit) {
        submitBtn?.setOnClickListener {
            lambda.invoke()
        }
    }

    fun setOnLikeClickListener(lambda: ()-> Unit) {
        likeLL?.setOnClickListener {
            lambda.invoke()
        }
    }

    fun setOnCancelClickListener(lambda: () -> Unit) {
        cancelBtn?.setOnClickListener {
            lambda.invoke()
        }
    }

    private fun setAnchor(padding: Int) {

        if (submitBtn != null && submitBtn!!.visibility == View.VISIBLE) {
            setPadding(submitBtn!!, padding)
        }

        if (likeLL != null && likeLL!!.visibility == View.VISIBLE) {
            setPadding(likeLL!!, padding)
        }

        if (cancelBtn != null && cancelBtn!!.visibility == View.VISIBLE) {
            setPadding(cancelBtn!!, padding)
        }
    }

    fun setLike(isLike: Boolean, count: Int) {
        setIcon(isLike)
        setCount(count)
    }

    fun setIcon(isLike: Boolean) {

        var res: Int = R.drawable.like
        if (isLike) {
            res = R.drawable.like1
        }
        likeIconIV?.setImageResource(res)
    }

    fun setCount(count: Int) {

        likeCountTV?.text = "${count}人"
    }

    fun setSubmitBtnTitle(title: String) {
        submitBtn?.setText(title)
    }

    private fun getBottomButtonPadding(): Int {
        val width = button_width.dpToPx(context)
        val padding: Int = (screenWidth - bottom_button_count * width) / (bottom_button_count + 1)

        return padding
    }

    private fun setPadding(view: View, padding: Int) {

        val p: MarginLayoutParams = view.layoutParams as MarginLayoutParams
        p.marginStart = padding
        view.layoutParams = p
    }

    fun changeSubmitToCancelBtn() {
        val style: Drawable? = getDrawable(context, R.drawable.button_cancel)
        submitBtn?.background = style
        submitBtn?.setTextColor(getColor(context, R.color.Primary_300))
    }

    fun changeSubmitToNormalBtn() {
        val style: Drawable? = getDrawable(context, R.drawable.button_rc)
        submitBtn?.background = style
        submitBtn?.setTextColor(getColor(context, R.color.MY_BLACK))
    }
}