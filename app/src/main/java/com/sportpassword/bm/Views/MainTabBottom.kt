package com.sportpassword.bm.Views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.sportpassword.bm.R
import com.sportpassword.bm.databinding.BottomViewBinding

class MainTabBottom: LinearLayout {

//        val a = BottomViewBinding.inflate(context.layoutInflater)
    val view: View = View.inflate(context, R.layout.bottom_view, this)
    private val binding: BottomViewBinding = BottomViewBinding.bind(view)

    private lateinit var teamTabContainer: LinearLayout

    constructor(context: Context): super(context) {
        println("aaa")
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        println("bbb")
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        println("ccc")
    }

    init {
        (context as? AppCompatActivity) ?. let {
            teamTabContainer = binding.teamTabContainer
            val i = 6
        }
    }

}
















