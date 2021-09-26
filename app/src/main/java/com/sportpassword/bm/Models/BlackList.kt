package com.sportpassword.bm.Models

import org.json.JSONObject


class BlackList(data: JSONObject) {

    var id: Int = -1
    var name: String = ""
    var mobile: String = ""
    var created_at: String = ""
    var token: String = ""
    var team: MutableMap<String, Any> = mutableMapOf()

}
class BlackLists(data: JSONObject) {
    var success: Boolean = true
    var rows: ArrayList<BlackList> = arrayListOf()

}
