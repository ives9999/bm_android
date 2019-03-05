package com.sportpassword.bm.Models

import org.json.JSONObject


class BlackList(data: JSONObject): SuperModel(data) {

    var id: Int = -1
    var name: String = ""
    var mobile: String = ""
    var created_at: String = ""
    var token: String = ""
    var team: MutableMap<String, Any> = mutableMapOf()

}
class BlackLists(data: JSONObject): SuperModel(data) {
    var success: Boolean = true
    var rows: ArrayList<BlackList> = arrayListOf()

}
