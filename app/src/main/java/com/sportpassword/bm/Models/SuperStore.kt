package com.sportpassword.bm.Models

import com.sportpassword.bm.Utilities.COURSE_KIND
import com.sportpassword.bm.Utilities.CYCLE_UNIT
import com.sportpassword.bm.Utilities.PRICE_UNIT
import com.sportpassword.bm.Utilities.noTime
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
    var address: String = ""
    var tel_text: String = ""
    var mobile_text: String = ""

    override fun filter() {
        super.filter()
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