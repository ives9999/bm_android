package com.sportpassword.bm.Models

import org.json.JSONObject

//class TempPlayDate(data:JSONObject): SuperModel(data) {
//    var success: Boolean = false
//        set(value) {
//            println("bbb")
//            field = value
//        }
//    var rows: ArrayList<String> = arrayListOf()
//        set(value) {
//            println("aaa")
//            field = value
//        }
//}
class TempPlayDate(val success:Boolean,val rows:ArrayList<String>) {
}

