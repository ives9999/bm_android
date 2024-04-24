package com.sportpassword.bm.extensions

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.sportpassword.bm.Models.City
import com.sportpassword.bm.Utilities.MEMBER_ARRAY

fun SharedPreferences.dump() {
    this.all.map {
        println("${it.key}=>${it.value}")
    }
}

fun SharedPreferences.has(key: String): Boolean {
    return this.contains(key)
}

fun SharedPreferences.resetMember() {
    for ((key, value) in MEMBER_ARRAY) {
        val hashMap: HashMap<String, String> = value as HashMap<String, String>
        val type: String = hashMap["type"] as String
        when (type) {
            "String" -> this.edit().putString(key, "").apply()
            "Int" -> this.edit().putInt(key, 0).apply()
            "Boolean" -> this.edit().putBoolean(key, false).apply()
        }
    }
    this.edit().putInt("id", -1).apply()
}

// return is [
//             ["id": "5", "name": "新北市"],
//             ["id": "6", "name": "台北市"]
//   ]
fun SharedPreferences.getAllCitys(): ArrayList<HashMap<String, String>> {
    val tmps: ArrayList<HashMap<String, String>> = arrayListOf()
    //this.edit().remove("citys").commit()
    val citys_string: String = this.getString("citys", "") as String
    //println(citys_string)

    if (citys_string.length > 0) {
        val jsonArray: JsonArray = JsonParser.parseString(citys_string).asJsonArray
//    println(jsonArray)
        val gson = Gson()
        for (_city in jsonArray) {
            val city: City = gson.fromJson(_city, City::class.java)
            //println("name:${city.name} => id:${city.id}")
            tmps.add(hashMapOf("id" to city.id.toString(), "name" to city.name))
        }
    }

    return tmps
}

// return is [
//             "52": [
//                     "id": "52",
//                     "name": ["新北市"],
//                     "rows": [
//                          ["id": "5", "name": "中和"],
//                          ["id": "6", "name": "永和"]
//                     ]
//                  ]
//           ]
fun SharedPreferences.getAreasFromCity(city_id: Int): ArrayList<HashMap<String, String>> {

    val allAreas = this.getAllAreas()
    for ((key, value) in allAreas) {
        if (key.toInt() == city_id) {
            val rows: ArrayList<HashMap<String, String>> = arrayListOf()
            if (value.containsKey("rows")) {
                val _rows = value["rows"] as ArrayList<*>
                for (_row in _rows) {
                    val row: HashMap<String, String> = hashMapOf()
                    val tmps = _row as LinkedTreeMap<*, *>
                    for ((key1, value1) in tmps) {
                        val _key1 = key1.toString()
                        val _value1 = value1.toString()
                        row[_key1] = _value1
                    }
                    rows.add(row)
                }
            }
            return rows
        }
    }

    val tmps: ArrayList<HashMap<String, String>> = arrayListOf()
    return tmps
}

fun SharedPreferences.getAllAreas(): HashMap<String, HashMap<String, Any>> {

    val areas_string: String = this.getString("areas", "") as String
    //println(areas_string)

    if (areas_string.length > 0) {
        val type = object : TypeToken<HashMap<String, HashMap<String, Any>>>() {}.type
        val jsonObject = Gson().fromJson<HashMap<String, HashMap<String, Any>>>(areas_string, type)
        return jsonObject
    } else {
        val tmps: HashMap<String, HashMap<String, Any>> = hashMapOf()
        return tmps
    }
}