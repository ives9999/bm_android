package com.sportpassword.bm.Models

import com.sportpassword.bm.Utilities.Global
import com.sportpassword.bm.Utilities.noSec
import com.sportpassword.bm.Utilities.telShow

class ArenasTable: Tables() {
    var rows: ArrayList<ArenaTable> = arrayListOf()
}

class ArenaTable: Table() {

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
    var manager_id: Int = -1
    var color: String = ""

    var area_show: String = ""
    var interval_show: String = ""
    var air_condition_show: String = ""
    var parking_show: String = ""
    var bathroom_show: String = ""
    var block_show: String = ""

    override fun filterRow() {
        
        super.filterRow()
        
        if (area_id > 0) {
            area_show = Global.zoneIDToName(area_id)
        }

        if (open_time != null && close_time != null && open_time.length > 0 && close_time.length > 0) {
            interval_show = open_time.noSec() + " ~ " + close_time.noSec()
        }
        
        when (air_condition) {
        1 -> air_condition_show = "有"
        0 -> air_condition_show = "無"
        else -> air_condition_show = "未提供"
        }
        
        if (parking > 0) {
            parking_show = "${parking}個"
        } else {
            parking_show = "未提供"
        }

        if (bathroom > 0) {
            bathroom_show = "${bathroom}個"
        } else {
            bathroom_show = "未提供"
        }

        if (block > 0) {
            block_show = "${block}個"
        } else {
            block_show = "未提供"
        }
        
        if (city_id > 0 && area_id > 0) {
            address = "${city_show}${area_show}${road}"
        }
    }
}