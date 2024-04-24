package com.sportpassword.bm.extensions

import android.widget.Button
import androidx.core.content.ContextCompat
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor

fun Button.setLook(backgroundColor: Int, textColor: Int) {
    this.backgroundColor = ContextCompat.getColor(context, backgroundColor)
    this.textColor = ContextCompat.getColor(context, textColor)
}
