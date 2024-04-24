package com.sportpassword.bm.extensions

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

fun Date.toMyString(pattern: String = "yyyy-MM-dd HH:mm:ss"): String {
    val formatter = SimpleDateFormat(pattern)
    return formatter.format(this)
}
fun Date.getY(): Int {
    val formatter = SimpleDateFormat("yyyy")
    val res = formatter.format(this).toInt()
    return res
}
fun Date.getm(): Int {
    val formatter = SimpleDateFormat("MM")
    val res = formatter.format(this).toInt()
    return res
}
fun Date.getd(): Int {
    val formatter = SimpleDateFormat("dd")
    val res = formatter.format(this).toInt()
    return res
}
fun Date.getH(): Int {
    val formatter = SimpleDateFormat("HH")
    val res = formatter.format(this).toInt()
    return res
}
fun Date.geti(): Int {
    val formatter = SimpleDateFormat("mm")
    val res = formatter.format(this).toInt()
    return res
}
fun Date.gets(): Int {
    val formatter = SimpleDateFormat("ss")
    val res = formatter.format(this).toInt()
    return res
}

fun Date.dateToWeekday(): Int {
    val c: Calendar = Calendar.getInstance()
    //month: 0 for 1月
    c.set(this.getY(), this.getm()-1, this.getd())
    //SUNDAY is 1, MONDAY is 2
    var weekday = c.get(Calendar.DAY_OF_WEEK)-1
    if (weekday == 0) { weekday = 7}

    return weekday
}

fun Date.dateToWeekdayForChinese(): String {

    val weekday = this.dateToWeekday()
    var res: String = "一"
    if (weekday == 1) {
        res = "一"
    } else if (weekday == 2) {
        res = "二"
    } else if (weekday == 3) {
        res = "三"
    } else if (weekday == 4) {
        res = "四"
    } else if (weekday == 5) {
        res = "五"
    } else if (weekday == 6) {
        res = "六"
    } else if (weekday == 7) {
        res = "日"
    }

    return res
}

fun Date.timeIntervalSince(stop: Date): Long {
    val diffInMs = this.time - stop.time
    return TimeUnit.MILLISECONDS.toSeconds(diffInMs)
}

fun Date.getMonthDays(_y: Int?=null, _m: Int?=null): Int {
    var y = _y
    if (y == null) {
        y = this.getY()
    }
    var m = _m
    if (m == null) {
        m = this.getm()
    }
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, y)
    calendar.set(Calendar.MONTH, m)
    val d = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    return d
}