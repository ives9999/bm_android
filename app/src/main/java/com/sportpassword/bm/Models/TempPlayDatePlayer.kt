package com.sportpassword.bm.Models

import com.sportpassword.bm.Utilities.toDateTime
import org.json.JSONObject

class TempPlayDatePlayer(data: JSONObject) {
    var id: Int = -1
    var member_id: Int = -1
    var team_id: Int = -1
    var name: String = ""
    var mobile: String = ""
    var play_date: String = ""
    var status: String = "on"
    var off_at: String = ""
    var token: String = ""
    var created_at: String = ""
    var updated_at: String = ""

    fun filterRow() {
//        _start_hour = start_time.toDateTime("HH:mm:ss").getH()
//        _end_hour = end_time.toDateTime("HH:mm:ss").getH()
//        _start_minute = start_time.toDateTime("HH:mm:ss").getm()
//        _end_minute = end_time.toDateTime("HH:mm:ss").getm()
//        _color = MYCOLOR.from(color)
    }
}

class TempPlayDatePlayers(data: JSONObject) {
    var success: Boolean = true
    var rows: ArrayList<TempPlayDatePlayer> = arrayListOf()
}