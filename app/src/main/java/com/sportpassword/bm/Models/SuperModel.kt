package com.sportpassword.bm.Models

import com.sportpassword.bm.Utilities.JSONParse
import com.sportpassword.bm.Utilities.isPrimitive
import com.sportpassword.bm.Utilities.print
import org.json.JSONObject
import java.io.Serializable
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

open class SuperModel(data: JSONObject): Serializable {

    open fun toHashMap() {

    }

    open fun filter() {

    }

    open fun <T: SuperModel> getSuperRows(): ArrayList<T>? {
        return null
    }

    fun print() {
        this::class.memberProperties.forEach {
            //println(it.name + " => " + it.getter.call(this))
            _print(it, this)
        }
    }

    private fun _print(it: KProperty1<out Any, Any?>, obj: Any, prefix: String="") {
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
                        } else {
                            println("${prefix}${it.name} => ${row} [")
                            row::class.memberProperties.forEach{ it1 ->
                                _print(it1, row, "    ")
                            }
                            println("]")
                        }
                    }
                }
            }
        } else {
            println("${prefix}${it.name} => " + it.getter.call(obj))
        }

    }
}
