package com.sportpassword.bm.Services

import com.sportpassword.bm.Models.City
import com.sportpassword.bm.Models.Coach
import com.sportpassword.bm.Utilities.CITY_KEY
import com.sportpassword.bm.Utilities.COACH_SENIORITY_KEY
import com.sportpassword.bm.Utilities.MOBILE_KEY
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by ives on 2018/2/23.
 */
object CoachService: DataService() {

    override val model: Coach = Coach(-1, "", "", "")


    override fun setData(id: Int, title: String, token: String, featured_path: String, vimeo: String, youtube: String): Coach {
        val data = Coach(id, title, token, featured_path, vimeo, youtube)
        return data
    }

    override fun setData1(obj: JSONObject): MutableMap<String, MutableMap<String, Any>> {
        model.dataReset()
        for ((key, value) in model.data) {
            if (obj.has(key)) {
                _jsonToData(obj, key, value)
            }
        }
        return model.data
    }

    override fun dealOne(json: JSONObject) {
        super.dealOne(json)
        for ((key, value) in model.data) {
            _jsonToData(json, key, value)
        }
    }

    override fun _jsonToData(tmp: JSONObject, key: String, item: Map<String, Any>) {
        //println(key)
        val type = item["vtype"] as String
        //println("type: $type")
        if (type == "Int") {
            try {
                if (key == COACH_SENIORITY_KEY) {
                    var show = "未提供"
                    if (tmp != JSONObject.NULL) {
                        show = tmp.getInt(key).toString() + "年"
                    }
                    model.data[key]!!["show"] = show
                } else {
                    model.data[key]!!["show"] = tmp.getInt(key).toString()
                }
                model.data[key]!!["value"] = tmp.getInt(key)
            } catch (e: JSONException) {
                //println(e.localizedMessage)
                model.data[key]!!["value"] = -1
                model.data[key]!!["show"] = "未提供"
            }
        } else if (type == "String") {
            var value: String = ""
            try {
                value = tmp.getString(key)
            } catch (e: JSONException) {

            }
            model.data[key]!!["value"] = value
            model.data[key]!!["show"] = value
            if (key == MOBILE_KEY) {
                model.mobileShow()
            }
        } else if (type == "array") {
            if (key == CITY_KEY) {
                try {
                    val obj1 = tmp.getJSONObject(key)
                    val id = obj1.getInt("id")
                    val name = obj1.getString("name")
                    val city = City(id, name)
                    model.updateCity(city)
                } catch (e: JSONException) {
                    println(e.localizedMessage)
                }
            }
        }
    }
}