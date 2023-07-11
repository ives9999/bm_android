package com.sportpassword.bm.extensions

import android.content.Context
import android.util.TypedValue
import org.jetbrains.anko.displayMetrics
import java.text.NumberFormat

fun Int.toTwoString(): String {

    val s: String = this.toString()
    val count: Int = s.length

    return when (count) {
        1 -> "0${s}"
        else -> s
    }
}

fun Int.quotientAndRemainder(dividingBy: Int): Pair<Int, Int> {
    val quotient = this / dividingBy
    val remainder = this % dividingBy
    val res: Pair<Int, Int> = quotient to remainder

    return res
}

fun Int.formattedWithSeparator(): String {
    return NumberFormat.getNumberInstance().format(this)
}

fun Int.dpToPx(context: Context): Int {

    return (TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        toFloat(),
        context.displayMetrics
    )).toInt()
}

fun Int.numberToChinese(): String {
    when (this) {
        1->
            return "一"
        2->
            return "二"
        3->
            return "三"
        4->
            return "四"
        5->
            return "五"
        6->
            return "六"
        7->
            return "七"
        8->
            return "八"
        9->
            return "九"
        10->
            return "十"
        else->
            return ""
    }
}