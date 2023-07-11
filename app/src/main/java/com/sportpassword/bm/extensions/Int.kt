package com.sportpassword.bm.extensions

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