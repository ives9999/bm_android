package com.sportpassword.bm.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.sportpassword.bm.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation


fun ImageView.avatar(path: String, isCircle: Boolean = false, radius: Int = 0) {

    val requestManager: RequestManager = Glide.with(context)
    var requestBuilder = requestManager.load(path)
    if (isCircle) {
        requestBuilder = requestBuilder.circleCrop()
    }

    if (radius > 0 && !isCircle) {
        requestBuilder = requestBuilder.transform(RoundedCorners(radius))
    }
    requestBuilder.placeholder(R.drawable.nophoto)
        .error(R.drawable.load_failed_square)
        .into(this)


//
//    var requestBuilder = if (path.isNotEmpty()) requestManager.load(path)
//    else requestManager.load(R.drawable.loading_square_120)
//
//    if (isCircle) {
//        requestBuilder = requestBuilder.centerCrop()
//    } else {
//        if (radius > 0) {
//            requestBuilder = requestBuilder.transform(RoundedCorners(radius))
//        }
//    }
//
//    requestBuilder.placeholder(R.drawable.loading_square_120)
//        .error(R.drawable.loading_square_120)
//        .into(this)
}

fun ImageView.featured(path: String, isCircle: Boolean = false, radius: Int = 0) {

    val requestManager: RequestManager = Glide.with(context)
    var requestBuilder = requestManager.load(path)
    if (isCircle) {
        requestBuilder = requestBuilder.circleCrop()
    }

    if (radius > 0 && !isCircle) {
        requestBuilder = requestBuilder.transform(RoundedCorners(radius))
    }
    requestBuilder.placeholder(R.drawable.nophoto)
        .error(R.drawable.load_failed_square)
        .into(this)
}

fun ImageView.setImage(name: String) {
    val id = context.resources.getIdentifier(name, "drawable", context.packageName)
    this.setImageResource(id)
}