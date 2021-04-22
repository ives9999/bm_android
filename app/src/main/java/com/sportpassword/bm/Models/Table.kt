package com.sportpassword.bm.Models

class Tables<T: Table> {
    var success: Boolean = true
    var page: Int = 0
    var totalCount: Int = 0
    var perPage: Int = 0
    var rows: ArrayList<T> = arrayListOf()
}

open class Table {

    var id: Int = -1
    var name: String = ""
    var title: String = ""
    var channel: String = ""
    var mobile: String = ""
    var city_id: Int = -1
    var slug: String = ""
    var status: String = "online"
    var token: String = ""
    var sort_order: Int = 0
    var pv: Int = 0
    var created_id: Int = 0
    var featured_path: String = ""
    var created_at: String = ""
    var updated_at: String = ""

    var created_at_show: String = ""
    var updated_at_show: String = ""

    var address: String = ""
    var city_show: String = ""
    var mobile_show: String = ""
}