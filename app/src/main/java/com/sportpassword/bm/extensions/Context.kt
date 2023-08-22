package com.sportpassword.bm.extensions

import android.content.Context
import androidx.core.content.ContextCompat

fun Context.getMyColor(color: Int): Int {
    return ContextCompat.getColor(this, color)
}