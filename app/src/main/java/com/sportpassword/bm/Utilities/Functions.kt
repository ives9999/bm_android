package com.sportpassword.bm.functions

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.content.pm.PackageInfoCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception
import java.lang.reflect.Type
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import androidx.core.app.ActivityCompat
import com.sportpassword.bm.Models.Area
import com.sportpassword.bm.Models.City
import com.sportpassword.bm.Models.Tables
import com.sportpassword.bm.Models.Tables2
import com.sportpassword.bm.Utilities.Zone
import com.sportpassword.bm.extensions.toDateTime
import com.sportpassword.bm.extensions.toMyString
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.pow


infix fun <T: Any?> Boolean?.then(block: () -> T): T? = if (this == true) block() else null

inline fun <reified U> genericType(): Type = object : TypeToken<U>() {}.type

inline fun <reified T: Tables> jsonToModels(jsonString: String): T? {

    var t: T? = null
    try {
        t = Gson().fromJson<T>(jsonString, T::class.java)
    } catch (e: java.lang.Exception) {
        //Global.message = e.localizedMessage
    }

    return t
}

inline fun <reified T: Tables2<U>, U> jsonToModels2(jsonString: String, typeToken: Type): T? {

    var t: T? = null
    //println(t)
    try {
        t = Gson().fromJson<T>(jsonString, typeToken)
    } catch (e: java.lang.Exception) {
        //Global.message = e.localizedMessage
        println(e.localizedMessage)
        val e1 = e.localizedMessage
    }

    return t
}
inline fun <reified U> jsonToModel(jsonString: String): U? {
    var u: U? = null
    try {
        val modelType: Type = genericType<U>()
        u = Gson().fromJson<U>(jsonString, modelType)
    } catch (e: Exception) {
        println(e.localizedMessage)
    }

    return u
}

inline fun <reified U> jsonToModelForList(jsonString: String, modelType: Type): U? {
    var u: U? = null
    try {
        u = Gson().fromJson<U>(jsonString, modelType)
    } catch (e: Exception) {
        println(e.localizedMessage)
    }

    return u
}

inline fun <reified U> jsonToModelForOne(jsonString: String): U? {

    var u: U? = null
    try {
        u = Gson().fromJson<U>(jsonString, U::class.java)
    } catch (e: java.lang.Exception) {
        //Global.message = e.localizedMessage
        println(e.localizedMessage)
    }

    return u
}

fun <T1, T2> Map<T1, T2>.print() {
    for ((key, value) in this) {
        println("$key => $value")
    }
}

fun <T> List<T>.print() {
    for (value in this) {
        //println("${value}")
    }
}

//inline fun <reified U> jsonToModelForOne(jsonString: String, modelType: Type): U? {
//
//    var u: U? = null
//    try {
//        u = Gson().fromJson<U>(jsonString, modelType)
//    } catch (e: java.lang.Exception) {
//        //Global.message = e.localizedMessage
//        println(e.localizedMessage)
//    }
//
//    return U
//}


infix fun Map<String, String>.mergeWith(anotherMap: Map<String, String>): Map<String, String> {
    val result = this.toMutableMap()
    anotherMap.forEach {
        var value = result[it.key]
        value = if (value == null || value == it.value) it.value else value + ", ${it.value}"
        result[it.key] = value
    }
    return result
}

infix fun HashMap<String, String>.mergeWith(anotherMap: HashMap<String, String>): HashMap<String, String> {
    val mutableMap: MutableMap<String, String> = this.toMutableMap()
    anotherMap.forEach {
        var value = mutableMap[it.key]
        value = if (value == null || value == it.value) it.value else value + ", ${it.value}"
        mutableMap[it.key] = value
    }
    return HashMap(mutableMap)
}

fun <R> readInstanceProperty(instance: Any, propertyName: String): R {
    val property = instance::class.members
        // don't cast here to <Any, R>, it would succeed silently
        .first { it.name == propertyName } as KProperty1<Any, *>
    // force a invalid cast exception if incorrect type here
    return property.get(instance) as R
}

fun getPropertyValue(instance: Any, propertyName: String): String {
    instance::class.memberProperties
        .forEach {
            val name = it.name
            if (name == propertyName) {
                val tmpValue = it.getter.call(instance)
                var value: String = ""
                when (tmpValue) {
                    is Int -> value = tmpValue.toString()
                    is Boolean -> value = tmpValue.toString()
                    is String -> value = tmpValue
                }
                return value
            }
        }
    return ""
}

fun getColor(context: Context, color: Int): Int {
    return ContextCompat.getColor(context, color)
}

fun getDrawable(context: Context, drawable: Int): Drawable? {
    return ContextCompat.getDrawable(context, drawable)
}

fun getResourceID(context: Context, string: String, source: String): Int {
    return context.resources.getIdentifier(string, source, context.packageName)
}

fun version(context: Context): String {
    val p = context.applicationContext.packageManager.getPackageInfo(
        context.packageName,
        0
    )
    val v = PackageInfoCompat.getLongVersionCode(p).toInt()
    val n = p.versionName
    val version: String = "v$n#$v"

    return version
}

//infix fun <T: Any?> Boolean?.then(block: () -> T): T? = if (this == true) block() else null
//
//infix fun Map<String, String>.mergeWith(anotherMap: Map<String, String>): Map<String, String> {
//    val result = this.toMutableMap()
//    anotherMap.forEach {
//        var value = result[it.key]
//        value = if (value == null || value == it.value) it.value else value + ", ${it.value}"
//        result[it.key] = value
//    }
//    return result
//}

fun isPermissionGranted(context: Context, permission: String): Boolean {
    val selfPermission: Int = ContextCompat.checkSelfPermission(context, permission)

    return selfPermission == PackageManager.PERMISSION_GRANTED
}

fun askForPermission(activity: Activity, permission: String, requestCode: Int) {
    ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
}

fun today(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.TAIWAN)
    return sdf.format(Date())
}

fun makeTimes(start_time: String="07:00", end_time: String="23:00", interval: Int=60): ArrayList<String> {

    val allTimes: ArrayList<String> = arrayListOf()
    var s = start_time.toDateTime("HH:mm")
    val e = end_time.toDateTime("HH:mm")
    if (s != null && e != null) {
        allTimes.add(start_time)
        while (s!!.compareTo(e) < 0) {
            val cal = Calendar.getInstance()
            cal.time = s
            cal.add(Calendar.MINUTE, interval)
            s = cal.time
            allTimes.add(s.toMyString("HH:mm"))
        }
    }

    return allTimes
}

fun weekdaysParse(value: Int): ArrayList<Int> {
    val arr: ArrayList<Int> = arrayListOf()
    if (value > 0 && value < 128) {
        for (i in 0..6) {
            val two = 2.toFloat().pow(i).toInt()
            if ((value and two) > 0) {
                arr.add(i + 1)
            }
        }
    }

    return arr
}

fun weekdaysToDBValue(arr: ArrayList<Int>): Int {
    var res: Int = 0
    for (value in arr) {
        if (value >= 0 && value <= 6) {
            res = res or (2.toFloat().pow(value - 1).toInt())
        }
    }

    return res
}

fun getMonthLastDay(year: Int, month: Int): Int {

    return Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
}

fun getCitys(): ArrayList<City> {

    val citys: ArrayList<City> = arrayListOf()
    for (zone in Zone.zones) {
        if (zone.containsKey("parent")) {
            val parent: Int = zone["parent"] as Int
            if (parent == 0) {
                var id: Int? = null
                var name: String? = null
                if (zone.containsKey("id")) {
                    id = zone["id"] as Int
                }
                if (zone.containsKey("name")) {
                    name = zone["name"] as String
                }
                if (id != null && name != null) {
                    val city = City(id, name)
                    citys.add(city)
                }
            }
        }
    }

    return citys
}

fun getAreasByCityID(city_id: Int): ArrayList<Area> {

    val areas: ArrayList<Area> = arrayListOf()
    for (zone in Zone.zones) {
        if (zone.containsKey("parent")) {
            val parent: Int = zone["parent"] as Int
            if (parent == city_id) {
                var id: Int? = null
                var name: String? = null
                if (zone.containsKey("id")) {
                    id = zone["id"] as Int
                }
                if (zone.containsKey("name")) {
                    name = zone["name"] as String
                }
                if (id != null && name != null) {
                    val area = Area(id, name)
                    areas.add(area)
                }
            }
        }
    }

    return areas
}

fun getScreenHeight(resources: Resources): Int {
    val displayMetrics = resources.displayMetrics
    val density = displayMetrics.density
    val screenHeight = displayMetrics.heightPixels

    return screenHeight
}

fun getScreenWidth(resources: Resources): Int {
    val displayMetrics = resources.displayMetrics
    val density = displayMetrics.density
    val screenWidth = displayMetrics.widthPixels

    return screenWidth
}

fun zoneIDToName(zone_id: Int): String {
    var value = ""
    for (zone in Zone.zones) {
        val id = zone["id"] as Int
        if (id == zone_id) {
            value = zone["name"] as String
            break
        }
    }
    return value
}

fun nowTime(): String {
    val sdf = SimpleDateFormat("hh:mm:ss", Locale.TAIWAN)
    return sdf.format(Date())
}


