package com.sportpassword.bm.Views

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.sportpassword.bm.R
import kotlinx.android.synthetic.main.tag.view.*
import org.jetbrains.anko.textResource

class Tag(context: Context) : LinearLayout(context) {

    // isSelected can't use because system has
    var isChecked: Boolean = false
    var key: String = ""
    var value: String = ""

    init {
        LayoutInflater.from(context).inflate(R.layout.tag, this)
        setSelectedStyle()
    }

    fun setSelectedStyle() {
        if (isChecked) {
            selectedStyle()
        } else {
            unSelectedStyle()
        }
    }

    fun selectedStyle() {
        tag_view.setBackgroundResource(R.drawable.tag_selected)
        //tag_view.setBackgroundResource(R.color.TAG_SELECTED_BACKGROUND)
        tag_view.setTextColor(ContextCompat.getColor(context, R.color.TAG_SELECTED_TEXTCOLOR))
    }

    fun unSelectedStyle() {
        tag_view.setBackgroundResource(R.drawable.tag_unselected)
        //tag_view.setBackgroundResource(R.color.TAG_UNSELECTED_BACKGROUND)
        tag_view.setTextColor(ContextCompat.getColor(context, R.color.TAG_UNSELECTED_TEXTCOLOR))
    }

}

















