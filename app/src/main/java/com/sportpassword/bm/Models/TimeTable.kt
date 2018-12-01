package com.sportpassword.bm.Models

import com.sportpassword.bm.Utilities.MYCOLOR
import com.sportpassword.bm.Utilities.getH
import com.sportpassword.bm.Utilities.toDate
import org.json.JSONObject

class TimeTable(data: JSONObject): SuperModel(data) {
    var success: Boolean = true
    var rows: ArrayList<Row> = arrayListOf()

    inner class Row(data: JSONObject): SuperModel(data) {
        var id: Int = -1
        var title: String = ""
        var day: Int = -1
        var start: String = ""
        var end: String = ""
        var limit: Int = -1
        var count: Int = 0
        var content: String = ""
        var color: String = ""
        var status: String = "online"
        var created_id: Int = 0
        var created_at: String = ""
        var updated_at: String = ""
        var _start: Int = 0
        var _end: Int = 0
        var _color: MYCOLOR = MYCOLOR.success

        fun filterRow() {
            _start = start.toDate("HH:mm:ss").getH()
            _end = end.toDate("HH:mm:ss").getH()
            _color = MYCOLOR.from(color)
        }
    }
}