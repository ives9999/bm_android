package com.sportpassword.bm.Views

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.sportpassword.bm.R

class TabSearch(context: Context) : LinearLayout(context) {

    var isChecked: Boolean = false
    var key: String = ""
    var value: String = ""

    init {
        LayoutInflater.from(context).inflate(R.layout.tab_search, this)
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
        val line: LinearLayout = findViewById(R.id.line)
        line.setBackgroundColor(ContextCompat.getColor(context, R.color.MY_GREEN))
    }

    fun unSelectedStyle() {
        val line: LinearLayout = findViewById(R.id.line)
        line.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
    }

}