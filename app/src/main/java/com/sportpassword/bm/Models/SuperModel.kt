package com.sportpassword.bm.Models

import com.sportpassword.bm.Utilities.JSONParse
import org.json.JSONArray
import org.json.JSONObject
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.javaSetter

//open class SuperModel() {
//}

open class SuperModel(data: JSONObject) {

    init {
        this::class.memberProperties.forEach {
            //initilize(it, data)
        }
    }

    protected fun initilize(it: KProperty1<out SuperModel, Any?>, data: JSONObject) {
        val key = it.name
        val dict = makeMap(data)
        val value = dict[key]
        when (value) {
            is Boolean, is Int, is Float, is Double, is String ->
                if (it is KMutableProperty<*>) {
                    it.javaSetter!!.invoke(this, value)
                } else {
                    val field = it.javaField
                    if (field != null) {
                        field.isAccessible = true
                        field.set(this, value)
                    } else {
                        println("error")
                    }
                }
            is JSONArray -> {
                val type = it.returnType.toString()
                val subType = getSubType(type)
                //println(subType)
                when (subType) {
                    "Boolean", "Int", "Float", "Double", "String" -> {

                    }
                    else -> {
                        val arr = value as JSONArray
                        val kc = getInnerClass(subType)
                        var rows: ArrayList<Any> = arrayListOf()
                        for (i in 0..arr.length()-1) {
                            var obj: Any? = null
                            if (kc != null) {
                                obj = JSONParse.newInstance(kc, data, this)
                                rows.add(obj)
                            }
                        }
                        if (it is KMutableProperty<*>) {
                            it.javaSetter!!.invoke(this, rows)
                        } else {
                            val field = it.javaField
                            if (field != null) {
                                field.isAccessible = true
                                field.set(this, rows)
                            } else {
                                println("error")
                            }
                        }
                    }
                }
            }
            is JSONObject ->
                    println("bbb")
        }
    }

    protected fun getSubType(type: String): String {
        var subType = ""
        val pattern = "<.*\\.([^>]*)>".toRegex()
        val b = pattern.containsMatchIn(type)
        //println(b)
        if (b) {
            val matchResult = pattern.find(type)
            val list = matchResult!!.destructured.toList()
            if (list.size > 0) {
                subType = list.get(0)
            }
        }
        return subType
    }

    protected fun makeMap(mapData: JSONObject): MutableMap<String, Any> {
        var d: MutableMap<String, Any> = mutableMapOf()
        val keys = mapData.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            d[key] = mapData[key]
        }
        return d
    }

    protected fun getInnerClass(name: String): KClass<*>? {
        var kc: KClass<*>? = null
        this::class.nestedClasses.forEach {
            if (it.simpleName == name) {
                kc = it
            }
        }
        return kc
    }

}
