package com.sportpassword.bm.Views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.sportpassword.bm.R
import com.sportpassword.bm.databinding.BottomViewBinding
import com.sportpassword.bm.extensions.setImage
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor

class MainTabBottom: LinearLayout {

//        val a = BottomViewBinding.inflate(context.layoutInflater)
    val view: View = View.inflate(context, R.layout.bottom_view, this)
    private val binding: BottomViewBinding = BottomViewBinding.bind(view)

//    private lateinit var teamTabContainer: LinearLayout
//    private lateinit var courseTabContainer: LinearLayout
//    private lateinit var memberTabContainer: LinearLayout
//    private lateinit var arenaTabContainer: LinearLayout
//    private lateinit var moreTabContainer: LinearLayout

    constructor(context: Context): super(context) {
        println("aaa")
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        setFocus(attrs)

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        println("ccc")
    }

    init {
        (context as? AppCompatActivity) ?. let {
        }
    }

    private fun setFocus(attrs: AttributeSet) {
        attrs.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.MainTabBottom, 0, 0)
            if (typedArray.hasValue(R.styleable.MainTabBottom_focus)) {
                typedArray.getString(R.styleable.MainTabBottom_focus) ?. let {
                    val tabContainerID: Int = resources.getIdentifier(it + "TabContainer", "id", context.packageName)
                    val tabTitleID: Int = resources.getIdentifier(it + "TabTitle", "id", context.packageName)
                    val tabLineID: Int = resources.getIdentifier(it + "TabLine", "id", context.packageName)
                    val tabIconID: Int = resources.getIdentifier(it + "TabIcon", "id", context.packageName)

                    view.findViewById<LinearLayout>(tabContainerID) ?. let { ll ->
                        val myColorGreen = ContextCompat.getColor(context, R.color.Primary_300)
                        ll.findViewById<TextView>(tabTitleID) ?. let {
                            it.textColor = myColorGreen
                        }

                        ll.findViewById<LinearLayout>(tabLineID) ?. let {
                            it.backgroundColor = myColorGreen
                        }

                        ll.findViewById<ImageView>(tabIconID) ?. let { image ->
                            image.setImage("ic_${it}_on_svg")
                        }
                    }
                }
            }
        }
    }
}
















