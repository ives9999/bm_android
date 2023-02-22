package com.sportpassword.bm.extensions

fun Int.toTwoString(): String {

    val s: String = this.toString()
    val count: Int = s.length

    return when (count) {
        1 -> "0${s}"
        else -> s
    }
}