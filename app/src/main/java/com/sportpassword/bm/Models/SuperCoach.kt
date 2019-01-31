package com.sportpassword.bm.Models

import org.json.JSONObject

class SuperCoach(data: JSONObject): SuperModel(data) {
    var id: Int = -1
    var name: String = ""
    var channel: String = ""
    var mobile: String = ""
    var email: String = ""
    var website: String = ""
    var fb: String = ""
    var youtube: String = ""
    var line: String = ""
    var seniority: Int = -1
    var exp: String = ""
    var feat: String = ""
    var license: String = ""
    var slug: String = ""
    var charge: String = ""
    var content: String = ""
    var status: String = "online"
    var color: String = ""
    var token: String = ""
    var manager_id: Int = 0
    var sort_order: Int = 0
    var pv: Int = 0
    var created_id: Int = 0
    var created_at: String = ""
    var updated_at: String = ""
}

class SuperCoaches(data: JSONObject): SuperModel(data) {
    var success: Boolean = true
    var rows: ArrayList<SuperCoach> = arrayListOf()
}