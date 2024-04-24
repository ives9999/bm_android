package com.sportpassword.bm.extensions

import android.view.View

fun View.getIDString(): String {
    return this.resources.getResourceName(this.id)
}