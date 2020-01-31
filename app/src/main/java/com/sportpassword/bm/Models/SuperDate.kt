package com.sportpassword.bm.Models

import org.json.JSONObject

class SuperDate(data: JSONObject): SuperModel(data) {
    var id: Int = -1
    var signup_dateable_id: Int = -1
    var signup_dateable_type: String = ""
    var date: String = ""
    var token: String = ""
}