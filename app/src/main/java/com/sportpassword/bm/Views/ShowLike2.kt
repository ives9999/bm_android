package com.sportpassword.bm.Views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.sportpassword.bm.Controllers.BaseActivity
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.setImage
import com.sportpassword.bm.Utilities.then

class ShowLike2@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0):
    LinearLayout(context, attrs, defStyleAttr) {

    val view: View = View.inflate(context, R.layout.show_like2, this)

    var likeIconIV: ImageView? = null
    var likeCountTV: TextView? = null

    var initCount: Int = 0
    var initLike: Boolean = false
    var isLike: Boolean = false
    //var count: Int = 0

    init {
        (context as? BaseActivity)?.let { delegate ->

            view.findViewById<ImageView>(R.id.likeIconIV) ?. let {
                likeIconIV = it
            }

            view.findViewById<TextView>(R.id.likeCountTV) ?. let {
                likeCountTV = it
            }
        }
    }

    fun setLike(isLike: Boolean, count: Int) {
        this.isLike = isLike
        this.initCount = count
        setIcon(isLike)
        setCount(count)
    }

    fun setIcon(isLike: Boolean) {

        var res: Int = R.drawable.like_out_svg
        if (isLike) {
            res = R.drawable.like_in_svg
        }
        likeIconIV?.setImageResource(res)
    }

    fun setCount(count: Int) {
        likeCountTV?.text = "$count"
    }

    fun setOnThisClickListener(lambda: ()-> Unit) {
        this.setOnClickListener {
            val oldIsLike: Boolean = this.isLike
            this.isLike = !this.isLike
            setIcon(this.isLike)
            val count: Int = ((oldIsLike) then {this.initCount--}) ?: this.initCount++
            setCount(count)
            lambda.invoke()
        }
    }
}