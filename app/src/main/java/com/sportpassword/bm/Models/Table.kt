package com.sportpassword.bm.Models

import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.Utilities.mobileShow
import kotlin.reflect.full.memberProperties

//因為 data class 不太適用於繼承，所以這邊就不用 data class的宣告
//class Tables<T: Table> {
open class Tables {
    var success: Boolean = true
    var page: Int = 0
    var totalCount: Int = 0
    var perPage: Int = 0
    //var rows: ArrayList<Table> = arrayListOf()
}

open class ReturnJson {
    val success: Boolean = false
    val msg: String = ""
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
    var content: String = ""
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
            tel_show = tel.telShow()
        }

        if (created_at.isNotEmpty()) {
            created_at_show = created_at.noTime()
        }

        if (updated_at.isNotEmpty()) {
            updated_at_show = updated_at.noTime()
        }
        if (content == null) {
            content = ""
        }
    }

    open fun printRow() {
        val kc = this::class
        kc.memberProperties.forEach {
            println("${it.name}: ${it.getter.call(this).toString()}")
        }
    }
}