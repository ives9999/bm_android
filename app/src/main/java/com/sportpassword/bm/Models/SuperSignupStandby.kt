package com.sportpassword.bm.Models

import org.json.JSONObject

class SuperSignupStandby(data: JSONObject): SuperModel(data) {
    var id: Int = -1
    var member_id: Int = -1
    var signup_able_id: Int = -1
    var signup_able_type: String = ""
    var able_date_id: Int = -1
    var cancel_deadline: String = ""
    var token: String = ""
    var created_at: String = ""
    var updated_at: String = ""
    var member_name: String = ""
}