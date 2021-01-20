package com.sportpassword.bm.Views

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.sportpassword.bm.R

class Tag(context: Context) : LinearLayout(context) {

    init {
        LayoutInflater.from(context).inflate(R.layout.tag, this)
    }

}