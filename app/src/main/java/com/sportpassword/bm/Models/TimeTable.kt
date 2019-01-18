package com.sportpassword.bm.Models

import com.sportpassword.bm.Utilities.MYCOLOR
import com.sportpassword.bm.Utilities.getH
import com.sportpassword.bm.Utilities.toDateTime
import org.json.JSONObject

class TimeTable(data: JSONObject): SuperModel(data) {
    var success: Boolean = true
    var rows: ArrayList<Row> = arrayListOf()

    inner class Row(data: JSONObject): SuperModel(data) {
        var id: Int = -1
        var title: String = ""
        var weekday: Int = -1
        var start_date: String = ""
        var end_date: String = ""
        var start_time: String = ""
        var end_time: String = ""
        var charge: Int = -1
        var limit: Int = -1
        var count: Int = 0
        var content: String = ""
        var color: String = ""
        var status: String = "online"
        var created_id: Int = 0
        var created_at: String = ""
        var updated_at: String = ""
        var _start_time: Int = 0
        var _end_time: Int = 0
        var _color: MYCOLOR = MYCOLOR.success

        fun filterRow() {
            _start_time = start_time.toDateTime("HH:mm:ss").getH()
            _end_time = end_time.toDateTime("HH:mm:ss").getH()
            _color = MYCOLOR.from(color)
        }
    }
}