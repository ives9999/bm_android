package com.sportpassword.bm.extensions

import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.sportpassword.bm.R
import org.jetbrains.anko.textColor
import com.sportpassword.bm.functions.then

fun TextView.selected() {
    val checkedColor = ContextCompat.getColor(context, R.color.Primary_300)
    this.setTextColor(checkedColor)
}

fun TextView.unSelected() {
    val uncheckedColor = ContextCompat.getColor(context, R.color.MY_WHITE)
    this.setTextColor(uncheckedColor)
}

fun TextView.setTextLook(size: Float, color: Int) {
    this.textSize = size
    this.setTextLookColor(color)
}

fun TextView.setTextLookColor(color: Int) {
    this.textColor = ContextCompat.getColor(context, color)
}

fun TextView.setSpecialTextColor(fullText : String, changeText : String, color: Int) {
    val spannableString: SpannableString = SpannableString(fullText)
    val foreColor: ForegroundColorSpan = ForegroundColorSpan(ContextCompat.getColor(context, color))

    val start: Int = fullText.indexOf(changeText)
    val end: Int = start + changeText.length + 1

    spannableString.setSpan(foreColor, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

    this.setText(spannableString)
}

fun TextView.setSpecialTextBold(fullText : String, changeText : String, ofSize: Int) {
    val spannableString: SpannableString = SpannableString(fullText)
    val bold: AbsoluteSizeSpan = AbsoluteSizeSpan(ofSize)

    val start: Int = fullText.indexOf(changeText)
    val end: Int = start + changeText.length + 1

    spannableString.setSpan(bold, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE)

    this.setText(spannableString)
}

fun TextView.setSpecialTextColorAndBold(fullText : String, changeText : String, color: Int, ofSize: Int, ofBold: Int) {
    val spannableString: SpannableString = SpannableString(fullText)

    val foreColor: ForegroundColorSpan = ForegroundColorSpan(ContextCompat.getColor(context, color))
    val size: AbsoluteSizeSpan = AbsoluteSizeSpan(ofSize)
    val bold: StyleSpan = StyleSpan(ofBold)

    var start: Int = fullText.indexOf(changeText)
    start = ((start < 0) then { 0 }) ?: start
    val end: Int = ((start > 0) then { start + changeText.length }) ?: 0

    spannableString.setSpan(foreColor, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    spannableString.setSpan(size, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    spannableString.setSpan(bold, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    this.setText(spannableString)
}