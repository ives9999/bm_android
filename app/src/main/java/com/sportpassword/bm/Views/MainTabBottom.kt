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
    var attrs: AttributeSet? = null

    private lateinit var teamTabContainer: LinearLayout
    private lateinit var courseTabContainer: LinearLayout
    private lateinit var memberTabContainer: LinearLayout
    private lateinit var arenaTabContainer: LinearLayout
    private lateinit var moreTabContainer: LinearLayout

    constructor(context: Context): super(context) {
        println("aaa")
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        this.attrs = attrs
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        println("ccc")
    }

    init {
        (context as? AppCompatActivity) ?. let {
            teamTabContainer = binding.teamTabContainer
            courseTabContainer = binding.courseTabContainer
            memberTabContainer = binding.memberTabContainer
            arenaTabContainer = binding.arenaTabContainer
            moreTabContainer = binding.moreTabContainer

            val i = 6
        }

        attrs ?. let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.MainTabBottom, 0, 0)
            if (typedArray.hasValue(R.styleable.MainTabBottom_teamContainer)) {
                typedArray.getString(R.styleable.MainTabBottom_teamContainer) ?. let {

                }
            }
        }
    }

}
















