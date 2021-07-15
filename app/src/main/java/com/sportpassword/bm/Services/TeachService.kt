package com.sportpassword.bm.Services

/**
 * Created by ivessun on 2018/3/1.
 */
object TeachService: DataService() {

//    override val model: Teach = Teach(-1, "", "")

//    override fun setData(id: Int, title: String, token: String, featured_path: String, vimeo: String, youtube: String): Teach {
//        val data = Teach(id, title, token, featured_path, vimeo, youtube)
//        return data
//    }
//
//    override fun setData1(obj: JSONObject): MutableMap<String, MutableMap<String, Any>> {
//        model.dataReset()
//        for ((key, value) in model.data) {
//            if (obj.has(key)) {
//                _jsonToData(obj, key, value)
//            }
//        }
//        return model.data
//    }
//
//    override fun _jsonToData(tmp: JSONObject, key: String, item: Map<String, Any>) {
//        //println(key)
//        val type = item["vtype"] as String
//        //println("type: $type")
//        if (type == "Boolean") {
//            try {
//                val value = tmp.getBoolean(key)
//                model.data[key]!!["value"] = value
//                val show = if (value) "有" else "無"
//                model.data[key]!!["show"] = show
//            } catch (e: JSONException) {
//                //println(e.localizedMessage)
//                model.data[key]!!["value"] = -1
//                model.data[key]!!["show"] = "未提供"
//            }
//
//        } else if (type == "Int") {
//            try {
//                model.data[key]!!["value"] = tmp.getInt(key)
//                model.data[key]!!["show"] = tmp.getInt(key).toString()
//            } catch (e: JSONException) {
//                //println(e.localizedMessage)
//                model.data[key]!!["value"] = -1
//                model.data[key]!!["show"] = "未提供"
//            }
//        } else if (type == "String") {
//            var value: String = ""
//            try {
//                value = tmp.getString(key)
//            } catch (e: JSONException) {
//
//            }
//            if (value.length == 0 || value == "null") {
//                value = "未提供"
//            }
//            model.data[key]!!["value"] = value
//            model.data[key]!!["show"] = value
//        } else if (type == "array") {
//
//        }
//    }

}