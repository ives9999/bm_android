package com.sportpassword.bm.extensions

import android.widget.ImageView
import com.sportpassword.bm.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

fun ImageView.avatar(path: String, isCircle: Boolean = true, rounded: Int = 0) {
    var res: RequestCreator = if (path.isNotEmpty()) Picasso.with(context).load(path)
    else Picasso.with(context).load(R.drawable.loading_square_120)

    if (isCircle) {
        res = res.transform(CropCircleTransformation())
    } else {
        if (rounded > 0) {
            res = res.transform(RoundedCornersTransformation(rounded, 0))
        }
    }

    res.placeholder(R.drawable.loading_square_120)
        .error(R.drawable.loading_square_120)
        .into(this)
}

fun ImageView.featured(path: String, isCircle: Boolean = true, rounded: Int = 0) {

    var res: RequestCreator = Picasso.with(context).load(path)

    if (isCircle) {
        res = res.transform(CropCircleTransformation())
    } else {
        if (rounded > 0) {
            res = res.transform(RoundedCornersTransformation(rounded, 0))
        }
    }

    res.placeholder(R.drawable.loading_square_120)
        .error(R.drawable.loading_square_120)
        .into(this)
}