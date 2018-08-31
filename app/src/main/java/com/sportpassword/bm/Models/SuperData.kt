package com.sportpassword.bm.Models

import com.sportpassword.bm.Utilities.*
import org.jetbrains.anko.db.NULL
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

/**
 * Created by ives on 2018/2/17.
 */
open class SuperData(val id: Int, val title: String, val token: String, val featured_path: String="", val vimeo: String="", val youtube: String="") {
    open var data: MutableMap<String, MutableMap<String, Any>> = mutableMapOf()

    open fun dataReset(){}

    open fun updateCity(city: City) {
        data[CITY_KEY]!!["value"] = city.id
        data[CITY_KEY]!!["show"] = city.name
        data[CITY_KEY]!!["sender"] = city.id
    }
    open fun updateArea(area: Area) {
        data[CITY_KEY]!!["value"] = area.id
        data[CITY_KEY]!!["show"] = area.name
        data[CITY_KEY]!!["sender"] = area.id
    }

    fun mobileShow(_mobile: String? = null) {
        var mobile = _mobile
        if (_mobile == null) {
            mobile = data[MOBILE_KEY]!!["value"] as String
        }
        mobile = mobile!!.mobileShow()
        data[MOBILE_KEY]!!["show"] = mobile
    }
    fun telOrMobileShow(_tel: String? = null) {
        var tel = _tel
        if (_tel == null) {
            tel = data[TEL_KEY]!!["value"] as String
        }
        tel = tel!!.telOrMobileShow()
        data[TEL_KEY]!!["show"] = tel
    }

    fun print() {
        this::class.memberProperties.forEach {
            //println(it.name + " => " + it.getter.call(this))
            _print(it, this)
        }
    }
    fun _print(it: KProperty1<out Any, Any?>, obj: Any, prefix: String="") {
        val type = it.returnType.toString()
        val tmps = JSONParse.getSubType(type)
        val subType = tmps.get("type")!!
        if (subType.isPrimitive()) {
            println("${prefix}${it.name} => " + it.getter.call(obj))
        } else if (subType == "Map") {
            println("${prefix}${it.name} => " + it.getter.call(obj))
        } else if (subType == "Array"){
            val rows = it.getter.call(obj) as ArrayList<Any>
            for (row in rows) {
                if (tmps.containsKey("value_type")) {
                    val valueType = tmps.get("value_type")
                    if (valueType!!.isPrimitive()) {
                        println("${prefix}${it.name} => ${row}")
                    } else {
                        val child = JSONParse.getInnerClass(this::class, valueType)
                        if (child != null) {
                            child.memberProperties.forEach{ it1 ->
                                _print(it1, row, child.simpleName+" : ")
                            }
                        }
                    }
                }
            }
        }
    }
}