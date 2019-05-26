package com.sportpassword.bm.Services

import android.content.Context
import com.sportpassword.bm.Models.SuperCourses
import com.sportpassword.bm.Utilities.CompletionHandler
import com.sportpassword.bm.Utilities.JSONParse
import com.sportpassword.bm.Utilities.URL_COURSE_LIST
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

object CourseService: DataService() {

    lateinit var superCourses: SuperCourses

    fun getList(context: Context, token: String?, filter: Array<Array<Any>>?, page: Int, perPage: Int, complete: CompletionHandler) {

        var url = URL_COURSE_LIST
        if (token != null) {
            url = url + "/" + token
        }
//        println(url)
        val header: MutableList<Pair<String, String>> = mutableListOf()
        header.add(Pair("Accept","application/json"))
        header.add(Pair("Content-Type","application/json; charset=utf-8"))

        val body = JSONObject()
        body.put("source", "app")
        body.put("channel", "bm")
        body.put("page", page.toString())
        body.put("perPage", perPage.toString())

        if (filter != null) {
            val whereArr = JSONArray()
            for (i in filter.indices) {
                val operateArr = JSONArray()
                for (item in filter[i]) {
                    operateArr.put(item)
                }
                whereArr.put(operateArr)
            }
            body.put("where", whereArr)
        }

        MyHttpClient.instance.post(context, url, body.toString()) { success ->
            if (success) {
                val response = MyHttpClient.instance.response
                if (response != null) {
                    try {
                        val json = JSONObject(response.toString())
                        superCourses = JSONParse.parse<SuperCourses>(json)!!
//                        for (row in superCourses.rows) {
//                            row.print()
//                        }
                        this.success = true
                    } catch (e: Exception) {
                        this.success = false
                        msg = "parse json failed，請洽管理員"
                    }
                    complete(this.success)
                } else {
                    println("response is null")
                }
            } else {
                msg = "網路錯誤，無法跟伺服器更新資料"
                complete(success)
            }
        }
    }
}