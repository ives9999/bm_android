package com.sportpassword.bm.Models

import com.sportpassword.bm.Utilities.Global
import com.sportpassword.bm.Utilities.noSec
import com.sportpassword.bm.Utilities.telShow

class StoresTable: Tables() {
    var rows: ArrayList<StoreTable> = arrayListOf()
}

class StoreTable: Table() {

    var tel: String = ""
    var fb: String = ""
    var website: String = ""
    var email: String = ""
    var line: String = ""

    var open_time: String = ""
    var close_time: String = ""

    var area_id: Int = -1
    var road: String = ""
    var zip: Int = -1

    var content: String = ""

    var tel_show: String = ""
    var open_time_show: String = ""
    var close_time_show: String = ""
    var interval_show: String = ""

    override fun filterRow() {
        super.filterRow()

        if (city_id > 0 && area_id > 0) {
            val city_name = Global.zoneIDToName(city_id)
            val area_name = Global.zoneIDToName(area_id)
            address = zip.toString() + city_name + area_name + road
        }

        if (tel != null && tel.length > 0) {
            tel_show = tel.telShow()
        }

        if (open_time != null && open_time.length > 0) {
            open_time_show = open_time.noSec()
        }

        if (close_time != null && close_time.length > 0) {
            close_time_show = close_time.noSec()
        }

        if (open_time != null && close_time != null && open_time.length > 0 && close_time.length > 0) {
            interval_show = "${open_time}~${close_time}"
        }
    }
}