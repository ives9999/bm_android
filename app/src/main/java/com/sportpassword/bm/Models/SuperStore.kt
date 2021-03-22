package com.sportpassword.bm.Models

import com.sportpassword.bm.Utilities.*
import org.json.JSONObject

class SuperStore(data: JSONObject): SuperModel(data) {
    var id: Int = -1
    var name: String = ""
    var channel: String = ""
    var slug: String = ""

    var tel: String = ""
    var mobile: String = ""
    var fb: String = ""
    var website: String = ""
    var email: String = ""
    var line: String = ""

    var open_time: String = ""
    var close_time: String = ""

    var city_id: Int = 0
    var area_id: Int = 0
    var road: String = ""
    var zip: Int = 0

    var content: String = ""
    var status: String = "online"
    var token: String = ""
    var sort_order: Int = 0
    var pv: Int = 0
    var created_id: Int = 0
    var created_at: String = ""
    var updated_at: String = ""
    var created_at_text: String = ""
    var featured_path: String = ""
    var thumb: String = ""

    var city: String = ""
    var open_time_text: String = ""
    var close_time_text: String = ""
    var tel_text: String = ""
    var mobile_text: String = ""

    var content_style: String = ""

    var managers: ArrayList<HashMap<String, Any>> = arrayListOf()

    var city_show: String = ""
    var area_show: String = ""
    var address: String = ""
    var tel_show: String = ""
    var mobile_show: String = ""
    var open_time_show: String = ""
    var close_time_show: String = ""

    override fun filter() {
        super.filter()
        if (city_id > 0) {
            city_show = Global.zoneIDToName(city_id)
            area_show = Global.zoneIDToName(area_id)
            address = zip.toString() + city_show + area_show + road
        }

        if (tel.length > 0) {
            tel_show = tel.telShow()
        }
        
        if (mobile.length > 0) {
            mobile_show = mobile.mobileShow()
        }
        
        if (open_time.length > 0) {
            open_time_show = open_time.noSec()
        }
        
        if (close_time.length > 0) {
            close_time_show = close_time.noSec()
        }

        created_at_text = created_at.noTime()
    }

}

class SuperStores(data: JSONObject): SuperModel(data) {
    var success: Boolean = true
    var page: Int = 0
    var totalCount: Int = 0
    var perPage: Int = 0
    var rows: ArrayList<SuperStore> = arrayListOf()
}