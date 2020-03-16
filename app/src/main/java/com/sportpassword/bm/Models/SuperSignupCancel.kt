package com.sportpassword.bm.Models

import org.json.JSONObject

class SuperSignupCancel(data: JSONObject): SuperModel(data) {

    var id: Int = -1
    var member_id: Int = -1
    var signupable_id: Int = -1
    var signupable_type: String = ""
    var able_date_id: Int = -1
    var token: String = ""
    var created_at: String = ""
    var updated_at: String = ""
    var member_name: String = ""
}