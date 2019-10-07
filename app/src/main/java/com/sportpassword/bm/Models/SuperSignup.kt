package com.sportpassword.bm.Models

import org.json.JSONObject

class SuperSignup(data: JSONObject): SuperModel(data) {
    var id: Int = -1
    var member_id: Int = -1
    var signupable_id: Int = -1
    var signupable_type: String = ""
    var able_date: String = ""
    var cancel_deadline: String = ""
    var status: String = ""
    var cancel_times: Int = 0
    var created_at: String = ""
    var updated_at: String = ""
    var member_name: String = ""
}

class SuperSignups(data: JSONObject): SuperModel(data) {
    var success: Boolean = true
    var page: Int = 0
    var totalCount: Int = 0
    var perPage: Int = 0
    var rows: ArrayList<SuperSignup> = arrayListOf()
}