package com.sportpassword.bm.Models

import android.text.InputType
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import org.jetbrains.anko.db.NULL
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

/**
 * Created by ives on 2018/2/17.
 */
open class SuperData(val id: Int, val title: String, val token: String, val featured_path: String="", val vimeo: String="", val youtube: String="") {

    open var data: MutableMap<String, MutableMap<String, Any>> = mutableMapOf()
    open var sections: ArrayList<String> = arrayListOf()
    open var rows: ArrayList<ArrayList<String>> = arrayListOf()
    open var textKeys: ArrayList<String> = arrayListOf()
    open var timeKeys: ArrayList<String> = arrayListOf()
    open var cat_id: Int = 21

    val transferPair: Map<String, String> = mapOf(CITY_KEY to "city_id",ARENA_KEY to "arena_id")

    open fun dataReset(){}

    open fun getDataRowWithKey(key: String): HashMap<String, Any> {
        var res = mutableMapOf<String, Any>()
        for ((key1, row) in data) {
            if (key == key1) {
                res = row
            }
        }

        return res.toMap() as HashMap<String, Any>
    }

    open fun updateCity(city: City? = null) {
        if (city != null) {
            data[CITY_KEY]!!["value"] = city.id
            data[CITY_KEY]!!["show"] = city.name
            data[CITY_KEY]!!["sender"] = city.id
        } else {
            data[CITY_KEY]!!["value"] = 0
            data[CITY_KEY]!!["show"] = ""
            data[CITY_KEY]!!["sender"] = 0
        }
    }
    open fun updateArea(area: Area) {
        data[CITY_KEY]!!["value"] = area.id
        data[CITY_KEY]!!["show"] = area.name
        data[CITY_KEY]!!["sender"] = area.id
    }

    open fun updateArena(arena: Arena? = null) {}
    open fun updateDegree(degrees: ArrayList<DEGREE>?=null) {}
    open fun updateWeekdays(days: ArrayList<Int>? = null) {}

    // android not use
    open fun initTextData() {
        for (key in textKeys) {
            updateText(key)
        }
    }

    open fun initTimeData() {
        for (key in timeKeys) {
            updateTime(key)
        }
    }

    open fun updateText(key: String, text: String?=null) {
        var _content = ""
        if (text != null) {
            _content = text
        }
        data[key]!!["value"] = _content
        textShow(key)
        setTextSender(key)
    }

    open fun textShow(key: String, length: Int=12) {
        var text: String = data[key]!!["value"] as String
        if (text.length > 0) {
            text = text.truncate(length)
        }
        data[key]!!["show"] = text
    }

    open fun setTextSender(key: String) {
        var res: MutableMap<String, Any> = mutableMapOf()
        val text: String = data[key]!!["value"] as String
        res["text"] = text
        val type = contentKey2Type(key)
        res["type"] = type
        data[key]!!["sender"] = res
    }

    // android not use
    open fun setAllTextSender() {
        for (key in textKeys) {
            setTextSender(key)
        }
    }

    open fun updateTime(key: String, time: String? = null) {
        if (time != null) {
            data[key]!!["value"] = time
        } else {
            data[key]!!["value"] = ""
        }
        timeShow(key)
        setTimeSender(key)
    }

    open fun timeShow(key: String) {
        var time = data[key]!!["value"] as String
        if (time.length > 0) {
            time = time.noSec()
        }
        data[key]!!["show"] = time
    }

    open fun setTimeSender(key: String) {
        var res: HashMap<String, Any> = hashMapOf()
        var time: String = data[key]!!["value"] as String
        if (time.length > 0) {
            time = time.noSec()
            val type = timeKey2Type(key)
            res["type"] = type
            res["time"] = time
        }
        data[key]!!["sender"] = res
    }

    open fun initTimeShow() {
        for (key in timeKeys) {
            timeShow(key)
        }
    }

    open fun makeSubmitArr(): MutableMap<String, Any> {
        var isAnyOneChange: Boolean = false
        var res: MutableMap<String, Any> = mutableMapOf()
        //println(data)

        for ((key, row) in data) {
            if (row.containsKey("submit")) {
                val isSubmit: Boolean = row["submit"] as Boolean
                var isChange: Boolean = false
                if (row.containsKey("change")) {
                    isChange = row["change"] as Boolean
                }
                if (isSubmit && isChange) {
                    res[key] = row["value"]!!
                    if (!isAnyOneChange) {
                        isAnyOneChange = true
                    }
                }
            }
        }

        if (!isAnyOneChange) {
            return res
        }
        var id: Int = -1
        if (data[ID_KEY]!!["value"] as Int > 0) {
            id = data[ID_KEY]!!["value"] as Int
        }
        if (id < 0) {
            res[MANAGER_ID_KEY] = member.id
            val cat_id: ArrayList<Int> = arrayListOf(cat_id)
            res[CAT_KEY] = cat_id
            res[SLUG_KEY] = data[NAME_KEY]!!["value"]!!
            res[CREATED_ID_KEY] = member.id
        } else {
            res[ID_KEY] = id
        }
        for ((key, value) in transferPair) {
            if (res.containsKey(key)) {
                res[value] = res[key]!!
                res.remove(key)
            }
        }

//        println(res)
        return res
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

    fun contentKey2Type(key: String): TEXT_INPUT_TYPE {
        var type = TEXT_INPUT_TYPE.content
        when (key) {
            TEAM_TEMP_CONTENT_KEY -> type = TEXT_INPUT_TYPE.temp_play
            CHARGE_KEY -> type = TEXT_INPUT_TYPE.charge
            CONTENT_KEY -> type = TEXT_INPUT_TYPE.content
            COACH_EXP_KEY -> type = TEXT_INPUT_TYPE.exp
            COACH_FEAT_KEY -> type = TEXT_INPUT_TYPE.feat
            COACH_LICENSE_KEY -> type = TEXT_INPUT_TYPE.license
        }
        return type
    }

    fun timeKey2Type(key: String): SELECT_TIME_TYPE {
        var type = SELECT_TIME_TYPE.play_start
        when (key) {
            TEAM_PLAY_START_KEY-> type = SELECT_TIME_TYPE.play_start
            TEAM_PLAY_END_KEY-> type = SELECT_TIME_TYPE.play_end

        }
        return type
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