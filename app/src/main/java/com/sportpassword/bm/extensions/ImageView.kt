package com.sportpassword.bm.extensions

import android.widget.ImageView
import com.sportpassword.bm.R
import com.squareup.picasso.Picasso

fun ImageView.avatar(path: String) {
    Picasso.with(context)
        .load(path)
        .placeholder(R.drawable.loading_square_120)
        .error(R.drawable.loading_square_120)
        .into(this)
}