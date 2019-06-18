package com.sportpassword.bm.Models

import org.json.JSONObject

class SuperCity(data: JSONObject): SuperModel(data) {
    var id: Int = -1
    var parent_id: Int = -1
    var name: String = ""
    var zip: String = ""
}

class SuperCitys(data: JSONObject): SuperModel(data) {
    var rows: ArrayList<SuperCity> = arrayListOf()
}