package com.sportpassword.bm.Models

import com.sportpassword.bm.Utilities.*
import org.json.JSONObject

class SuperCourse(data: JSONObject): SuperModel(data) {
    var id: Int = -1
    var title: String = ""
    var channel: String = ""
    var slug: String = ""
    var coach_id: Int = -1
    var price: Int = -1
    var price_unit: String = ""
    var price_unit1: PRICE_UNIT = PRICE_UNIT.month
    var price_desc: String = ""
    var price_text_long: String = ""
    var price_text_short: String = ""
    var people_limit: Int = -1
    var people_limit_text: String = ""
    var kind: String = ""
    var kind1: COURSE_KIND = COURSE_KIND.cycle
    var kind_text: String = ""
    var cycle: Int = -1
    var cycle_unit: String = ""
    var cycle_unit1: CYCLE_UNIT = CYCLE_UNIT.month
    var start_date: String = ""
    var end_date: String = ""
    var weekday: Int = -1
    var weekday_text: String = ""
    var start_time: String = ""
    var end_time: String = ""
    var start_time_text: String = ""
    var end_time_text: String = ""
    var youtube: String = ""


    var content: String = ""
    var content_style: String = ""
    var status: String = "online"
    var token: String = ""
    var sort_order: Int = 0
    var pv: Int = 0
    var created_id: Int = 0
    var created_at: String = ""
    var created_at_text: String = ""
    var updated_at: String = ""
    var featured_path: String = ""
    var thumb: String = ""
    var coach: SuperCoach = SuperCoach(JSONObject())
    var citys: SuperCitys = SuperCitys(JSONObject())
    var nextCourseTime: HashMap<String, String> = hashMapOf()
    //var signups: SuperSignups = SuperSignups()
    var isSignup: Boolean = false
    var signup_id: Int = 0

    override fun filter() {
        super.filter()
        if (price_unit.count() > 0) {
            price_unit1 = PRICE_UNIT.from(price_unit)
        }
        if (cycle_unit.count() > 0) {
            cycle_unit1 = CYCLE_UNIT.from(cycle_unit)
        }
        if (kind.count() > 0) {
            kind1 = COURSE_KIND.from(kind)
        }
        created_at_text = created_at.noTime()
    }
}

class SuperCourses(data: JSONObject): SuperModel(data) {
    var success: Boolean = true
    var page: Int = 0
    var totalCount: Int = 0
    var perPage: Int = 0
    var rows: ArrayList<SuperCourse> = arrayListOf()
}