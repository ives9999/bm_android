package com.sportpassword.bm.Utilities

import com.beust.klaxon.internal.firstNotNullResult
import com.sportpassword.bm.Models.BlackList
import com.sportpassword.bm.Models.SuperCity
import com.sportpassword.bm.Models.SuperModel
import org.json.JSONArray
import org.json.JSONObject
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.javaSetter

class JSONParse {
    companion object {
        inline fun <reified T> parse(data: JSONObject): T? = _parse(T::class, data) as T?

        fun _parse(kc: KClass<*>, data: JSONObject): Any {

            val res = newInstance(kc, data)
            //return res
            setter(kc, data, res)

            return res!!
        }

        fun setter(kc: KClass<*>, data: JSONObject, res: Any) {
            val d = makeMap(data)
            //println(d)
            kc.memberProperties.forEach{
                val key = it.name
                val value = d[key]
                when (value) {
                    is Boolean, is Int, is Float, is Double, is String ->
                        _setter(it, res, value)
                    is JSONArray -> {
                        val type = it.returnType.toString()
                        val tmps = getSubType(type)
                        val subType = tmps.get("type")!!
                        when (subType) {
                            "Boolean", "Int", "Float", "Double", "String" -> {

                            }
                            else -> {
                                if (tmps.containsKey("value_type")) {
                                    val valueType = tmps.get("value_type")!!
                                    val rows = setRows(kc, it, res, value, valueType)
                                    _setter(it, res, rows)
                                }
                            }
                        }
                    }
                    is JSONObject -> {
                        if (key == "city") {
                            val city = JSONParse.parse<SuperCity>(value)!!
                            _setter(it, res, city)
                        }
                        //val d = makeMap(value)
                        //_setter(it, res, d)
                    }
                }
            }
        }

        private fun _setter(it: KProperty1<out Any, Any?>, obj: Any, value: Any) {
            if (it is KMutableProperty<*>) {
                it.javaSetter!!.invoke(obj, value)
            } else {
                val field = it.javaField
                if (field != null && obj != null) {
                    field.isAccessible = true
                    field.set(obj, value)
                } else {
                    println("error")
                }
            }
        }

        private fun setRows(kc: KClass<*>, it: KProperty1<out Any, Any?>, obj: Any, value: Any, subType: String): ArrayList<Any> {
            val packageName = kc.java.`package`.name
            val child = Class.forName(packageName+"."+subType).kotlin
            //val child = getInnerClass(kc, subType)
            val rows: ArrayList<Any> = arrayListOf()
            if (child != null) {
                val arr = value as JSONArray
                for (i in 0..arr.length() - 1) {
                    val j = arr[i] as JSONObject
                    val row = newInstance(child, j)
                    //val row = newInstance(child, j, obj)
                    setter(child, j, row)
                    rows.add(row)
                }
            }
            return rows
        }

         fun newInstance(kc: KClass<*>, data: JSONObject, obj: Any? = null): Any {
            var res: Any? = null
            val con = kc.constructors.firstOrNull()
            try {
                con!!.isAccessible = true
                if (obj == null) {
                    res = con!!.call(data)
                } else {
                    res = con!!.call(obj, data)
                }
            } catch (e: Exception) {
                println(e.localizedMessage )
            }
            return res!!
        }
        private fun makeMap(mapData: JSONObject): MutableMap<String, Any> {
            var d: MutableMap<String, Any> = mutableMapOf()
            val keys = mapData.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                d[key] = mapData[key]
            }
            return d
        }
        fun getSubType(type: String): MutableMap<String, String> {
            var subType = type
            var res: MutableMap<String, String> = mutableMapOf()
            val pattern = "^kotlin\\.([^\\.]+)[\\.]?([^\\.^<]*)[<]?([^>]*)[>]?".toRegex()
            val b = pattern.containsMatchIn(subType)
            if (b) {
                val matchRes = pattern.find(subType)
                if (matchRes != null) {
                    val arr = matchRes.groupValues
//                    for (s in arr) {
//                        println(s)
//                    }
                    if (arr[2].length == 0) {
                        subType = arr[1]
                    } else {
                        subType = arr[2]
                        var e = arr[3]
                        if (subType.contains("Map")) {
                            subType = "Map"
                            e = e.replace("kotlin.", "")
                            e = e.replace(" ", "")
                            //println(e)
                            val tmps = e.split(",")
                            if (tmps.size > 1) {
                                res.set("key_type", tmps[0])
                                res.set("value_type", tmps[1])
                            }
                        } else if (subType.contains("Array")) {
                            subType = "Array"
                            val pattern1 = "^.*\\.([^\\.]*)$".toRegex()
                            val b1 = pattern1.containsMatchIn(e)
                            if (b1) {
                                val matchRes1 = pattern1.find(e)
                                if (matchRes1 != null) {
                                    val arr1 = matchRes1.groupValues
                                    //arr1.print()
                                    if (arr1.size > 1) {
                                        res.set("value_type", arr1[1])
                                    }
                                }
                            }
                        }
                     }
                }
            }
            res.set("type", subType)


            /*
            if (type.contains("Map")) {
                subType = "Map"
            } else {
                var pattern = "<.*\\.([^>]*)>".toRegex()
                var b = pattern.containsMatchIn(type)
                //println(b)
                if (!b) {
                    pattern = ".*\\.([^>]*)".toRegex()
                    b = pattern.containsMatchIn(type)
                }
                if (b) {
                    val matchResult = pattern.find(type)
                    val list = matchResult!!.destructured.toList()
                    if (list.size > 0) {
                        subType = list.get(0)
                    }
                }
            }
            */

            return res
        }

        fun getInnerClass(parent: KClass<*>, name: String): KClass<*>? {
            var kc: KClass<*>? = null
            parent.nestedClasses.forEach {
                if (it.simpleName == name) {
                    kc = it
                }
            }
            return kc
        }

        fun getType(it: KProperty1<out SuperModel, Any?>): String {
            val type = it.returnType.toString()
            val tmps = getSubType(type)
            val subType = tmps.get("type")!!

            return subType
        }

        fun <T> getValue(fieldName: String, superModel: SuperModel, it: KProperty1<out SuperModel, Any?>): T? {
            val value = it.getter.call(superModel) as T?
            return value
        }
    }

}











