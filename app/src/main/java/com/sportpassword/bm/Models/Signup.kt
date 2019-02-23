package com.sportpassword.bm.Models

import org.json.JSONObject

class Signup(data: JSONObject): SuperModel(data) {
    var id: Int = -1
    var member_id: Int = -1
    var signupable_id: Int = -1
    var signupable_type: String = ""
    var status: String = ""
    var cancel_times: Int = 0
    var created_at: String = ""
    var updated_at: String = ""
}