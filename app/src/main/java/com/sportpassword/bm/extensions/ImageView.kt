package com.sportpassword.bm.extensions

import android.widget.ImageView
import com.sportpassword.bm.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation


fun ImageView.avatar(path: String, isCircle: Boolean = true, rounded: Int = 0) {

//    Picasso.get()
//        .load(path)
//        .placeholder(R.drawable.noavatar)
//        .error(R.drawable.load_failed_square)
//        .into(this)

    var res: RequestCreator = if (path.isNotEmpty()) Picasso.get().load(path)
    else Picasso.get().load(R.drawable.loading_square_120)

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

    var res: RequestCreator = Picasso.get().load(path)

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

fun ImageView.setImage(name: String) {
    val id = context.resources.getIdentifier(name, "drawable", context.packageName)
    this.setImageResource(id)
}