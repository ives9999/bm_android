package com.sportpassword.bm.Utilities

import com.beust.klaxon.internal.firstNotNullResult
import com.sportpassword.bm.Models.BlackList
import com.sportpassword.bm.Models.SuperModel
import org.json.JSONArray
import org.json.JSONObject
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
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
            var d = makeMap(data)
            //println(d)
            kc.memberProperties.forEach{
                val key = it.name
                val value = d[it.name]
                when (value) {
                    is Boolean, is Int, is Float, is Double, is String ->
                        _setter(it, res, value)
                    is JSONArray -> {
                        val type = it.returnType.toString()
                        val subType = getSubType(type)
                        when (subType) {
                            "Boolean", "Int", "Float", "Double", "String" -> {

                            }
                            else -> {
                                val rows = setRows(kc, it, res, value, subType)
                                _setter(it, res, rows)
                            }
                        }
                    }
                    is JSONObject -> {
                        val d = makeMap(value)
                        _setter(it, res, d)
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
            val child = getInnerClass(kc, subType)
            var rows: ArrayList<Any> = arrayListOf()
            if (child != null) {
                val arr = value as JSONArray
                for (i in 0..arr.length() - 1) {
                    val j = arr[i] as JSONObject
                    val row = newInstance(child, j, obj)
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
        private fun getSubType(type: String): String {
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

        private fun getInnerClass(parent: KClass<*>, name: String): KClass<*>? {
            var kc: KClass<*>? = null
            parent.nestedClasses.forEach {
                if (it.simpleName == name) {
                    kc = it
                }
            }
            return kc
        }
    }

}











