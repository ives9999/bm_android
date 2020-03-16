package com.sportpassword.bm.Models

import org.json.JSONObject

class SuperSignupDate(data: JSONObject): SuperModel(data) {

    var id: Int = -1
    var signupable_id: Int = -1
    var signupable_type: String = ""
    var date: String = ""
    var token: String = ""
}