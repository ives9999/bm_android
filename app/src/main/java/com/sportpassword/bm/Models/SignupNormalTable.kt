package com.sportpassword.bm.Models

import com.sportpassword.bm.Utilities.*

class SignupNormalTable: Table() {

    var member_id: Int = -1
    var signupable_id: Int = -1
    var signupable_type: String = ""
    var able_date_id: Int = -1
    var cancel_deadline: String = ""
    var member_name: String = ""
    var member_token: String = ""

    var ableTable: AbleTable? = null
    var memberTable: MemberTable? = null
    var dateTable: DateTable? = null

    override fun filterRow() {
        super.filterRow()

        if (created_at != null && created_at.isNotEmpty()) {
            created_at_show = created_at.noSec()
        }

        if (updated_at != null && updated_at.isNotEmpty()) {
            updated_at_show = updated_at.noSec()
        }

        val tmp = created_at.toDateTime()?.let {
            val weekday: String = it.dateToWeekdayForChinese()
            created_at_show = created_at_show + "(" + weekday + ")"
        }

        if (ableTable != null) {
            ableTable!!.filterRow()
        }

        if (memberTable != null) {
            memberTable!!.filterRow()
        }

        if (status.isNotEmpty()) {
            status_show = SIGNUP_STATUS.stringToSwift(status).toString()
        }
    }
}

class AbleTable: Table() {

    var arena_name: String = ""
    var arena_id: Int = 0
    var weekdays: Int = 0
    var play_start: String = ""
    var play_end: String = ""

    var weekdays_show: String = ""
    var interval_show: String = ""

    override fun filterRow() {
        super.filterRow()

        if ((play_start != null && play_start.isNotEmpty()) && (play_end != null && play_end.isNotEmpty())) {
            interval_show = play_start.noSec() + " ~ " + play_end.noSec()
        }

        if (weekdays > 0) {
            var weekdays_string: ArrayList<String> = arrayListOf()
            var i: Int = 1
            while (i <= 7) {
                val n: Int = Math.pow(2.0, i.toDouble()).toInt()
                if (weekdays and n > 0) {
                    weekdays_string.add(WEEKDAY.intToString(i))
                }
                i++
            }
            weekdays_show = weekdays_string.joinToString(",")
        }
    }
}