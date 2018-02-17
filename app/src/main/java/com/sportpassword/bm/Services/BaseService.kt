package com.sportpassword.bm.Services

import org.json.JSONObject

/**
 * Created by ives on 2018/2/15.
 */
open class BaseService {

    fun toJsonString(json: JSONObject, filter: Array<Array<Any>>) : String {
        var str = "{"

        val arr = json.names()
        for (i in 0..arr.length()-1) {
            val key = arr[i].toString()
            str += """ "$key":"${json.get(key)}" """
            if (i < arr.length()-1) {
                str += ","
            }
        }
        if (filter.isNotEmpty()) {
            str += """ ,"where": ["""
            for (i in filter.indices) {
                str += "["
                var j = 0
                for (item in filter[i]) {
                    str += """ "$item" """
                    if (j < filter[i].size-1) {
                        str += ","
                    }
                    j++
                }
                str += "]"
                if (i < filter.size-1) {
                    str += ","
                }
            }
            str += "]"
        }
        str += "}"

        return str
    }
}