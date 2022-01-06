package com.sportpassword.bm.Models

import com.google.gson.annotations.SerializedName
import com.sportpassword.bm.Utilities.*

class CoursesTable: Tables() {
    var rows: ArrayList<CourseTable> = arrayListOf()
}

class CourseTable: Table() {

    var coach_id: Int = -1
    var price: Int = -1
    var price_unit: String = ""
    var price_desc: String = ""
    var price_text_short: String = ""
    var price_text_long: String = ""
    var people_limit: Int = -1
    var people_limit_text: String = ""
    var kind: String = ""
    var kind_text: String = ""

    var cycle: Int = -1
    var cycle_unit: String = ""
    var weekday: Int = -1
    var weekday_text: String = ""
    var weekday_arr: ArrayList<Int> = arrayListOf()
    var start_date: String = ""
    var end_date: String = ""
    var start_time: String = ""
    var end_time: String = ""
    var deadline: String = ""
    var youtube: String = ""

    var content_style: String = ""
    var created_at_text: String = ""
    var coach: CoachTable? = null
    var dateTable: DateTable? = null
    @SerializedName("signup_normal_models") var signupNormalTables: ArrayList<SignupNormalTable> = arrayListOf()
    @SerializedName("signup_standby_models") var signupStandbyTables: ArrayList<SignupStandbyTable> = arrayListOf()

    //var nextCourseTime: [String: String] = [String: String]()
    var isSignup: Boolean = false
    var signup_id: Int = 0

    var start_time_show: String = ""
    var end_time_show: String = ""
    var price_unit_show: String = ""
    var price_long_show: String = ""
    var price_short_show: String = ""
    var people_limit_show: String = ""
    var kind_show: String = ""
    var cycle_unit_show: String = ""
    var weekdays_show: String = ""
    var interval_show: String = ""
    var signup_count_show: String = ""

    override fun filterRow() {
        super.filterRow()

        if (youtube == null) {
            youtube = ""
        }

        if (start_date == null) {
            start_date = ""
        }

        if (end_date == null) {
            end_date = ""
        }

        if (price_unit != null && price_unit.length > 0) {
            price_unit_show = PRICE_UNIT.from(price_unit).value
        }

        if (kind != null && kind.length > 0) {
            kind_show = COURSE_KIND.from(kind).value
        }

        if (cycle_unit != null && cycle_unit.length > 0) {
            cycle_unit_show = CYCLE_UNIT.from(cycle_unit).value
        }

        if (start_time.length > 0) {
            start_time_show = start_time.noSec()
        }

        if (end_time.length > 0) {
            end_time_show = end_time.noSec()
        }

        if (start_time.length > 0 && end_time.length > 0) {
            interval_show = start_time_show + " ~ " + end_time_show
        }

        if (weekday_arr.size > 0) {
            val show: ArrayList<String> = arrayListOf()
            for (weekday in weekday_arr) {
                val tmp: String = WEEKDAY.intToString(weekday)
                show.add(tmp)
            }
            weekdays_show = show.joinToString(",")
        }

        if (people_limit > 0) {
            people_limit_show = "可報名:${people_limit}位"
        } else {
            people_limit_show = "未提供報名"
        }

        if (signupNormalTables.size > 0) {
            signup_count_show = "${signupNormalTables.size}位"
        } else {
            signup_count_show = "0位"
        }

        if (coach != null) {
            mobile = coach!!.mobile
            mobile_show = mobile.mobileShow()
            coach!!.filterRow()
        }
    }
}