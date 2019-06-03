package com.sportpassword.bm.Models

import com.sportpassword.bm.Utilities.COURSE_KIND
import com.sportpassword.bm.Utilities.CYCLE_UNIT
import com.sportpassword.bm.Utilities.PRICE_CYCLE_UNIT
import org.json.JSONObject

class SuperCourse(data: JSONObject): SuperModel(data) {
    var id: Int = -1
    var title: String = ""
    var channel: String = ""
    var slug: String = ""
    var coach_id: Int = -1
    var price: Int = -1
    var price_cycle_unit: String = ""
    var price_cycle_unit1: PRICE_CYCLE_UNIT = PRICE_CYCLE_UNIT.month
    var price_desc: String = ""
    var limit: Int = -1
    var kind: String = ""
    var kind1: COURSE_KIND = COURSE_KIND.cycle
    var cycle: Int = -1
    var cycle_unit: String = ""
    var cycle_unit1: CYCLE_UNIT = CYCLE_UNIT.month
    var start_date: String = ""
    var end_date: String = ""
    var weekday: Int = -1
    var start_time: String = ""
    var end_time: String = ""
    var youtube: String = ""


    var content: String = ""
    var status: String = "online"
    var token: String = ""
    var sort_order: Int = 0
    var pv: Int = 0
    var created_id: Int = 0
    var created_at: String = ""
    var updated_at: String = ""
    var featured_path: String = ""
    var thumb: String = ""
    var coach: SuperCoach = SuperCoach(JSONObject())

    override fun filter() {
        super.filter()
        if (price_cycle_unit.count() > 0) {
            price_cycle_unit1 = PRICE_CYCLE_UNIT.from(price_cycle_unit)
        }
        if (cycle_unit.count() > 0) {
            cycle_unit1 = CYCLE_UNIT.from(cycle_unit)
        }
        if (kind.count() > 0) {
            kind1 = COURSE_KIND.from(kind)
        }

    }
}

class SuperCourses(data: JSONObject): SuperModel(data) {
    var success: Boolean = true
    var rows: ArrayList<SuperCourse> = arrayListOf()
}