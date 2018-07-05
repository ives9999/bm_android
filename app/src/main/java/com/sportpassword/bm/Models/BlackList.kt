package com.sportpassword.bm.Models

import org.json.JSONObject


/*
class BlackList(val success:Boolean, val rows:ArrayList<Row_BlackList>): SuperModel() {
}

class Row_BlackList(val name:String,val created_at:String,val team_id:Int,val token:String,val id:Int,val member_id:Int): SuperModel(){}

class Team_BlackList(val name:String,val token:String):SuperModel(){}
        */
class BlackList(data: JSONObject): SuperModel(data) {
    var success: Boolean = false
    var rows: ArrayList<Row> = arrayListOf()

    inner class Row(data: JSONObject): SuperModel(data) {
        var id: Int = -1
        var name: String = ""
        var mobile: String = ""
        var created_at: String = ""
        var token: String = ""
        var team: MutableMap<String, Any> = mutableMapOf()
    }
}
class Row_BlackList{}
