package com.sportpassword.bm.Models

import com.sportpassword.bm.Utilities.Global
import com.sportpassword.bm.Utilities.noSec
import com.sportpassword.bm.Utilities.telShow

class ArenasTable: Tables() {
    var rows: ArrayList<ArenaTable> = arrayListOf()
}

class ArenaTable: Table() {

    var tel: String = ""
    var fb: String = ""
    var website: String = ""
    var email: String = ""
    var open_time: String = ""
    var close_time: String = ""
    var block: Int = -1
    var area_id: Int = -1
    var road: String = ""
    var zip: Int = -1
    var air_condition: Int = -1
    var parking: Int = -1
    var bathroom: Int = -1
    var charge: String = ""
    var content: String = ""
    var manager_id: Int = -1
    var color: String = ""

    var area_show: String = ""
    var tel_show: String = ""
    var interval_show: String = ""
    var air_condition_show: String = ""
    var parking_show: String = ""

    override fun filterRow() {
        
        super.filterRow()
        
        if (area_id > 0) {
            area_show = Global.zoneIDToName(area_id)
        }
        
        if (tel.length > 0) {
            tel_show = tel.telShow()
        }
        
        if (open_time.length > 0 && close_time.length > 0) {
            interval_show = open_time.noSec() + " ~ " + close_time.noSec()
        }
        
        when (air_condition) {
        1 -> air_condition_show = "空調：有"
        0 -> air_condition_show = "空調：無"
        else -> air_condition_show = "未提供"
        }
        
        if (parking > 0) {
            parking_show = "${parking}個"
        } else {
            parking_show = "未提供"
        }
        
        if (city_id > 0 && area_id > 0) {
            address = "${city_show}${area_show}${road}"
        }
    }
}