package com.sportpassword.bm.Models

import com.sportpassword.bm.extensions.noSec
import com.sportpassword.bm.functions.zoneIDToName

class StoresTable: Tables() {
    var rows: ArrayList<StoreTable> = arrayListOf()
}

class StoreTable: Table() {

    var fb: String = ""
    var website: String = ""
    var email: String = ""
    var line: String = ""

    var open_time: String = ""
    var close_time: String = ""

    var area_id: Int = -1
    var road: String = ""
    var zip: Int = -1

    var open_time_show: String = ""
    var close_time_show: String = ""
    var interval_show: String = ""

    override fun filterRow() {
        super.filterRow()

        if (city_id > 0 && area_id > 0) {
            val city_name = zoneIDToName(city_id)
            val area_name = zoneIDToName(area_id)
            address = zip.toString() + city_name + area_name + road
        }

        if (open_time != null && open_time.length > 0) {
            open_time_show = open_time.noSec()
        }

        if (close_time != null && close_time.length > 0) {
            close_time_show = close_time.noSec()
        }

        if (open_time != null && close_time != null && open_time.length > 0 && close_time.length > 0) {
            interval_show = "${open_time_show}~${close_time_show}"
        }

        if (line == null) {
            line = "未提供"
        }

        if (website == null) {
            website = "未提供"
        }

        if (email == null) {
            email = "未提供"
        }
    }
}