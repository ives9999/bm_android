package com.sportpassword.bm.Models

import com.sportpassword.bm.Utilities.MYCOLOR
import com.sportpassword.bm.Utilities.getH
import com.sportpassword.bm.Utilities.getm
import com.sportpassword.bm.Utilities.toDateTime
import org.json.JSONObject

class Timetables(data: JSONObject): SuperModel(data) {
    var success: Boolean = true
    var rows: ArrayList<Timetable> = arrayListOf()
}

class Timetable(data: JSONObject): SuperModel(data) {
    var id: Int = -1
    var title: String = ""
    var weekday: Int = -1
    var weekday_text: String = ""
    var start_date: String = ""
    var end_date: String = ""
    var start_time: String = ""
    var start_time_text: String = ""
    var end_time: String = ""
    var end_time_text: String = ""
    var charge: Int = -1
    var limit: Int = -1
    var limit_text: String = ""
    var signup_count: Int = 0
    var count: Int = 0
    var content: String = ""
    var content_style: String = ""
    var color: String = ""
    var status: String = "online"
    var pv: Int = 0
    var created_id: Int = 0
    var created_at: String = ""
    var updated_at: String = ""
    var signups: ArrayList<Signup> = arrayListOf()

    var _start_hour: Int = 0
    var _end_hour: Int = 0
    var _start_minute: Int = 0
    var _end_minute: Int = 0
    var _color: MYCOLOR = MYCOLOR.success

    fun filterRow() {
        _start_hour = start_time.toDateTime("HH:mm:ss")!!.getH()
        _end_hour = end_time.toDateTime("HH:mm:ss")!!.getH()
        _start_minute = start_time.toDateTime("HH:mm:ss")!!.getm()
        _end_minute = end_time.toDateTime("HH:mm:ss")!!.getm()
        _color = MYCOLOR.from(color)
    }
}