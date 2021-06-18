package com.sportpassword.bm.Models

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
    var signup_normal_models: ArrayList<SignupNormalTable> = arrayListOf()
    var signup_standby_models: ArrayList<SignupStandbyTable> = arrayListOf()

    //var nextCourseTime: [String: String] = [String: String]()
    var isSignup: Boolean = false
    var signup_id: Int = 0

    var start_time_show: String = ""
    var end_time_show: String = ""
    var price_long_show: String = ""
    var price_short_show: String = ""
    var people_limit_show: String = ""
    var kind_show: String = ""
    var weekdays_show: String = ""
    var interval_show: String = ""
    var signup_count_show: String = ""

    override fun filterRow() {
        super.filterRow()

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

        if (signup_normal_models.size > 0) {
            signup_count_show = "${signup_normal_models.size}位"
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