package com.sportpassword.bm.Views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.sportpassword.bm.R
import com.sportpassword.bm.extensions.setImage

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
        view.findViewById<ImageView>(R.id.likeIconIV) ?. let {
            likeIconIV = it
        }

        view.findViewById<TextView>(R.id.likeCountTV) ?. let {
            likeCountTV = it
        }
    }

    fun setLike(isLike: Boolean, count: Int) {
        this.isLike = isLike
        this.initCount = count
        setIcon(isLike)
        setCount(count)
    }

    fun setIcon(isLike: Boolean) {

        var res: String = "like_out_svg"
        if (isLike) {
            res = "like_in_svg"
        }
        likeIconIV?.setImage(res)
    }

    fun setCount(count: Int) {
        likeCountTV?.text = "$count"
    }

    fun setOnThisClickListener(lambda: ()-> Unit) {
        this.setOnClickListener {
            val oldIsLike: Boolean = this.isLike
            this.isLike = !this.isLike
            setIcon(this.isLike)
            if (oldIsLike) {
                this.initCount--
            } else {
                this.initCount++
            }
            //val count: Int = ((oldIsLike) then {this.initCount--}) ?: this.initCount++
            setCount(initCount)
            lambda.invoke()
        }
    }
}