package com.sportpassword.bm.Services

import com.sportpassword.bm.Models.Arena
import com.sportpassword.bm.Models.City
import org.json.JSONException
import org.json.JSONObject
import com.sportpassword.bm.Utilities.*

object ArenaService: DataService() {

    override val model: Arena = Arena(-1, "", "", "")

    override fun setData(id: Int, title: String, token: String, featured_path: String, vimeo: String, youtube: String): Arena {
        val data = Arena(id, title, token, featured_path)
        return data
    }

    override fun setData1(obj: JSONObject): MutableMap<String, MutableMap<String, Any>> {
        model.dataReset()
        for ((key, value) in model.data) {
            if (obj.has(key)) {
                _jsonToData(obj, key, value)
            }
        }
        model.updateInterval()
        return model.data
    }

    override fun _jsonToData(tmp: JSONObject, key: String, item: Map<String, Any>) {
        //println(key)
        val type = item["vtype"] as String
        //println("type: $type")
        if (type == "Boolean") {
            try {
                val value = tmp.getBoolean(key)
                model.data[key]!!["value"] = value
                val show = if (value) "有" else "無"
                model.data[key]!!["show"] = show
            } catch (e: JSONException) {
                //println(e.localizedMessage)
                model.data[key]!!["value"] = -1
                model.data[key]!!["show"] = "未提供"
            }
            try {
                val value = tmp.getInt(key)
                var b: Boolean = false
                if (value > 0) { b = true}
                model.data[key]!!["value"] = b
                val show = if (b) "有" else "無"
                model.data[key]!!["show"] = show
            } catch (e: JSONException) {
                //println(e.localizedMessage)
                model.data[key]!!["value"] = -1
                model.data[key]!!["show"] = "未提供"
            }

        } else if (type == "Int") {
            try {
                model.data[key]!!["value"] = tmp.getInt(key)
                model.data[key]!!["show"] = tmp.getInt(key).toString()
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
            if (value.length == 0 || value == "null") {
                value = "未提供"
            }
            model.data[key]!!["value"] = value
            model.data[key]!!["show"] = value
            if (key == TEL_KEY) {
                model.telOrMobileShow()
            }
            if (key == ARENA_OPEN_TIME_KEY) {
                model.updateOpenTime(value)
            } else if (key == ARENA_CLOSE_TIME_KEY) {
                model.updateCloseTime(value)
            }
        } else if (type == "array") {

        }
    }
}