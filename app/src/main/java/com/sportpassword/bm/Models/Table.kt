package com.sportpassword.bm.Models

import com.sportpassword.bm.Utilities.BASE_URL
import com.sportpassword.bm.Utilities.Global
import com.sportpassword.bm.Utilities.mobileShow
import com.sportpassword.bm.Utilities.noTime

//因為 data class 不太適用於繼承，所以這邊就不用 data class的宣告
//class Tables<T: Table> {
open class Tables {
    var success: Boolean = true
    var page: Int = 0
    var totalCount: Int = 0
    var perPage: Int = 0
    //var rows: ArrayList<T> = arrayListOf()
}

open class Table {

    var id: Int = -1
    var name: String = ""
    var title: String = ""
    var channel: String = ""
    var tel: String = ""
    var mobile: String = ""
    var city_id: Int = -1
    var slug: String = ""
    var status: String = "online"
    var token: String = ""
    var sort_order: Int = 0
    var pv: Int = 0
    var like: Boolean = false
    var like_count: Int = 0
    var created_id: Int = 0
    var featured_path: String = ""
    var created_at: String = ""
    var updated_at: String = ""

    var created_at_show: String = ""
    var updated_at_show: String = ""

    var address: String = ""
    var city_show: String = ""
    var mobile_show: String = ""
    var tel_show: String = ""

    open fun filterRow() {
        if (featured_path.isNotEmpty()) {
            if (!featured_path.startsWith("http://") && !featured_path.startsWith("https://")) {
                featured_path = BASE_URL + featured_path
                //print(featured_path)
            }
        }

        if (city_id > 0) {
            city_show = Global.zoneIDToName(city_id)
        }

        if (mobile != null && mobile.isNotEmpty()) {
            mobile_show = mobile.mobileShow()
        }

        if (tel != null && tel.isNotEmpty()) {
            tel_show = tel.mobileShow()
        }

        if (created_at.isNotEmpty()) {
            created_at_show = created_at.noTime()
        }

        if (updated_at.isNotEmpty()) {
            updated_at_show = updated_at.noTime()
        }
    }

//    public fun printRow() {
//        let mirror: Mirror? = Mirror(reflecting: self)
//        for property in mirror!.children {
//            print("\(property.label ?? "")=>\(property.value)")
//        }
//    }
}