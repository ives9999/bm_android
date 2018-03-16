package com.sportpassword.bm.Models

import android.text.InputType
import com.sportpassword.bm.Services.DataService
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.*

/**
 * Created by ives on 2018/2/14.
 */
class Team(id: Int, name: String, token: String, featured_path: String, vimeo: String="", youtube: String=""): Data(id, name, token, featured_path, vimeo, youtube) {

    val more = "more"
    val none = "none"
    val defaultPad = InputType.TYPE_CLASS_TEXT
    val numberPad = InputType.TYPE_CLASS_NUMBER
    val phonePad = InputType.TYPE_CLASS_PHONE
    val emailPad = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
    var TO_CITY: String = "toCity"
    var TO_ARENA: String = "toArena"
    var TO_DAY: String = "toDay"
    var TO_SELECT_TIME: String = "toSelectTime"
    var TO_TEXT_INPUT: String = "toTextInput"
    var TO_SELECT_DEGREE: String = "toSelectDegree"
    override var data: MutableMap<String, MutableMap<String, Any>> = mutableMapOf()
    var lists: ArrayList<Map<String, Map<String, Any>>> = arrayListOf()


    override fun dataReset() {
        val _data = mutableMapOf<String, MutableMap<String, Any>>(
                TEAM_ID_KEY to mutableMapOf("ch" to "編號","vtype" to "Int","value" to -1),
                TEAM_CHANNEL_KEY to mutableMapOf("ch" to "頻道","vtype" to "String","value" to "","submit" to false,"show" to ""),
                TEAM_WEBSITE_KEY to mutableMapOf("ch" to "網站","vtype" to "String","value" to "","submit" to false,"show" to ""),
                TEAM_FB_KEY to mutableMapOf("ch" to "FB","vtype" to "String","value" to "","submit" to false,"show" to ""),
                TEAM_YOUTUBE_KEY to mutableMapOf("ch" to "youtube","vtype" to "String","value" to "","submit" to false,"show" to ""),
                TEAM_SLUG_KEY to mutableMapOf("ch" to "插槽","vtype" to "String","value" to "","submit" to true,"show" to ""),
                TEAM_MANAGER_ID_KEY to mutableMapOf("ch" to "管理者編號","vtype" to "Int","value" to -1,"submit" to false,"show" to ""),
                TEAM_TEMP_QUANTITY_KEY to mutableMapOf("ch" to "臨打人數缺額","vtype" to "Int","value" to -1,"submit" to false,"show" to ""),
                TEAM_TEMP_STATUS_KEY to mutableMapOf("ch" to "臨打狀態","vtype" to "String","value" to "","submit" to false,"show" to ""),
                TEAM_PV_KEY to mutableMapOf("ch" to "瀏覽數","vtype" to "Int","value" to -1,"submit" to false,"show" to ""),
                TEAM_TOKEN_KEY to mutableMapOf("ch" to "球隊token","vtype" to "String","value" to "","submit" to false,"show" to ""),
                TEAM_CREATED_ID_KEY to mutableMapOf("ch" to "建立者","vtype" to "Int","value" to -1,"submit" to false,"show" to ""),
                TEAM_CREATED_AT_KEY to mutableMapOf("ch" to "建立時間","vtype" to "String","value" to "","submit" to false,"show" to ""),
                TEAM_UPDATED_AT_KEY to mutableMapOf("ch" to "最後一次修改時間","vtype" to "String","value" to "","submit" to false,"show" to ""),
                TEAM_THUMB_KEY to mutableMapOf("ch" to "代表圖","vtype" to "String","value" to "","submit" to false,"show" to ""),
                TEAM_NEAR_DATE_KEY to mutableMapOf("ch" to "下次臨打日期","vtype" to "String","value" to "","submit" to false,"show" to ""),
                TEAM_TEMP_SIGNUP_KEY to mutableMapOf("ch" to "已報名人數","vtype" to "String","value" to "","submit" to false,"show" to ""),
                TEAM_NAME_KEY to mutableMapOf("ch" to "名稱","vtype" to "String","value" to "","submit" to true,"atype" to none,"show" to "","keyboardType" to defaultPad),
                TEAM_LEADER_KEY to mutableMapOf("ch" to "聯絡人","vtype" to "String","value" to "","submit" to true,"atype" to none,"show" to "","keyboardType" to defaultPad),
                TEAM_MOBILE_KEY to mutableMapOf("ch" to "電話","vtype" to "String","value" to "","submit" to true,"atype" to none,"show" to "","keyboardType" to phonePad),
                TEAM_EMAIL_KEY to mutableMapOf("ch" to "EMail","vtype" to "String","value" to "","submit" to true,"atype" to none,"show" to "","keyboardType" to emailPad),
                TEAM_PLAY_START_KEY to mutableMapOf("ch" to "開始時間","vtype" to "String","value" to "","submit" to true,"atype" to more,"segue" to TO_SELECT_TIME,"sender" to mutableMapOf<String, Any>(),"show" to ""),
                TEAM_PLAY_END_KEY to mutableMapOf("ch" to "結束時間","vtype" to "String","value" to "","submit" to true,"atype" to more,"segue" to TO_SELECT_TIME,"sender" to mutableMapOf<String, Any>(),"show" to ""),
                TEAM_BALL_KEY to mutableMapOf("ch" to "使用球種","vtype" to "String","value" to "","submit" to true,"atype" to none,"show" to "","keyboardType" to defaultPad),
                TEAM_DEGREE_KEY to mutableMapOf("ch" to "球隊程度","vtype" to "array","value" to mutableListOf<String>(),"submit" to true,"atype" to more,"segue" to TO_SELECT_DEGREE,"sender" to arrayOf<String>(),"show" to ""),
                TEAM_CHARGE_KEY to mutableMapOf("ch" to "收費說明","vtype" to "String","value" to "","submit" to true,"atype" to more,"segue" to TO_TEXT_INPUT,"sender" to mutableMapOf<String, Any>(),"show" to ""),
                TEAM_CONTENT_KEY to mutableMapOf("ch" to "球隊說明","vtype" to "String","value" to "","submit" to true,"atype" to more,"segue" to TO_TEXT_INPUT,"sender" to mutableMapOf<String, Any>(),"show" to ""),
                TEAM_TEMP_FEE_M_KEY to mutableMapOf("ch" to "臨打費用：男","vtype" to "Int","value" to -1,"submit" to true,"atype" to none,"show" to "","keyboardType" to numberPad),
                TEAM_TEMP_FEE_F_KEY to mutableMapOf("ch" to "臨打費用：女","vtype" to "Int","value" to -1,"submit" to true,"atype" to none,"show" to "","keyboardType" to numberPad),
                TEAM_TEMP_CONTENT_KEY to mutableMapOf("ch" to "臨打說明","vtype" to "String","value" to "","submit" to true,"atype" to more,"segue" to TO_TEXT_INPUT,"sender" to mutableMapOf<String, Any>(),"show" to ""),
                TEAM_CITY_KEY to mutableMapOf("ch" to "區域","vtype" to "array","value" to 0,"submit" to true,"atype" to more,"segue" to TO_CITY,"sender" to 0,"show" to ""),
                TEAM_ARENA_KEY to mutableMapOf("ch" to "球館","vtype" to "array","value" to 0,"submit" to true,"atype" to more,"segue" to TO_ARENA,"sender" to mutableMapOf<String, Int>(),"show" to ""),
                TEAM_DAYS_KEY to mutableMapOf("ch" to "星期幾","vtype" to "array","value" to mutableListOf<Int>(),"submit" to true,"atype" to more,"segue" to TO_DAY,"sender" to mutableListOf<Int>(),"show" to ""),
                TEAM_FEATURED_KEY to mutableMapOf("ch" to "代表圖","vtype" to "String","value" to "","path" to "","submit" to false,"show" to "")
        )
        data = _data
    }

    fun updateCity(city: City) {
        data[TEAM_CITY_KEY]!!["value"] = city.id
        data[TEAM_CITY_KEY]!!["show"] = city.name
        data[TEAM_CITY_KEY]!!["sender"] = city.id
        setArenaSender()
    }

    fun updateArena(arena: Arena) {
        data[TEAM_ARENA_KEY]!!["value"] = arena.id
        data[TEAM_ARENA_KEY]!!["show"] = arena.name
        setArenaSender()
    }

    fun updateDays(days: ArrayList<Int>) {
        data[TEAM_DAYS_KEY]!!["value"] = days
        daysShow()
        setDaysSender()
    }

    fun updateDegree(degrees: ArrayList<String>) {
        data[TEAM_DEGREE_KEY]!!["value"] = degrees
        degreeShow()
        setDegreeSender()
    }

    fun updatePlayStartTime(time: String? = null) {
        if (time != null) {
            data[TEAM_PLAY_START_KEY]!!["value"] = time
        }
        val tmp: String = data[TEAM_PLAY_START_KEY]!!["value"] as String
        data[TEAM_PLAY_START_KEY]!!["show"] = tmp.noSec()
        setPlayStartTimeSender()
    }
    fun updatePlayEndTime(time: String? = null) {
        if (time != null) {
            data[TEAM_PLAY_END_KEY]!!["value"] = time
        }
        val tmp: String = data[TEAM_PLAY_END_KEY]!!["value"] as String
        data[TEAM_PLAY_END_KEY]!!["show"] = tmp.noSec()
        setPlayEndTimeSender()
    }
    fun updateTempContent(content: String? = null) {
        if (content != null) {
            data[TEAM_TEMP_CONTENT_KEY]!!["value"] = content
        }
        tempContentShow()
        setTempContentSender()
    }
    fun updateCharge(content: String? = null) {
        if (content != null) {
            data[TEAM_CHARGE_KEY]!!["value"] = content
        }
        chargeShow()
        setChargeSender()
    }
    fun updateContent(content: String? = null) {
        if (content != null) {
            data[TEAM_CONTENT_KEY]!!["value"] = content
        }
        contentShow()
        setContentSender()
    }
    fun updateNearDate(n1: String? = null, n2: String? = null) {
        var nn1: String = ""
        var nn2: String = ""
        if (n1 != null) {
            data[TEAM_NEAR_DATE_KEY]!!["value"] = n1
            nn1 = n1!!
        } else {
            nn1 = (data[TEAM_NEAR_DATE_KEY]!!["value"] as String)
        }
        if (n2 != null) {
            data[TEAM_NEAR_DATE_KEY]!!["value1"] = n2
            nn2 = n2!!
        } else {
            nn2 = (data[TEAM_NEAR_DATE_KEY]!!["value1"] as String)
        }
        val n: String = nn1 + "(" + nn2 + ")"
        data[TEAM_NEAR_DATE_KEY]!!["show"] = n
    }

    fun daysShow() {
        val row: Map<String, Any> = data[TEAM_DAYS_KEY]!!
        val days = row["value"] as ArrayList<Int>
        if (days.size > 0) {
            var res: ArrayList<String> = arrayListOf<String>()
            for (i in 0..days.size-1) {
                val day = days[i]
                for (j in 0..DAYS.size-1) {
                    val item = DAYS[j]
                    val idx: Int = item["value"] as Int
                    val text: String = item["text"] as String
                    if (idx == day) {
                        res.add(text)
                    }
                }
            }
            val show: String = res.joinToString(", ")
            data[TEAM_DAYS_KEY]!!["show"] = show
        }
    }

    fun degreeShow() {
        val degrees: ArrayList<String> = data[TEAM_DEGREE_KEY]!!["value"] as ArrayList<String>
        var res: ArrayList<String> = arrayListOf<String>()
        for (i in 0..degrees.size-1) {
            val degree = degrees[i]
            val type: DEGREE = DEGREE.valueOf(degree)
            val text: String = type.value
            res.add(text)
        }
        data[TEAM_DEGREE_KEY]!!["show"] = res.joinToString(", ")
    }
    fun tempContentShow(length: Int=15) {
        var text: String = data[TEAM_TEMP_CONTENT_KEY]!!["value"] as String
        text = text.truncate(length)
        data[TEAM_TEMP_CONTENT_KEY]!!["show"] = text
    }
    fun chargeShow(length: Int=15) {
        var text: String = data[TEAM_CHARGE_KEY]!!["value"] as String
        text = text.truncate(length)
        data[TEAM_CHARGE_KEY]!!["show"] = text
    }
    fun contentShow(length: Int=15) {
        var text: String = data[TEAM_CONTENT_KEY]!!["value"] as String
        text = text.truncate(length)
        data[TEAM_CONTENT_KEY]!!["show"] = text
    }
    fun feeShow() {
        var text: String = ""
        text = data[TEAM_TEMP_FEE_M_KEY]!!["show"] as String + "元"
        data[TEAM_TEMP_FEE_M_KEY]!!["show"] = text
        text = data[TEAM_TEMP_FEE_F_KEY]!!["show"] as String + "元"
        data[TEAM_TEMP_FEE_F_KEY]!!["show"] = text
    }

    fun setArenaSender() {
        var arena_sender: MutableMap<String, Int> = mutableMapOf<String, Int>()
        val city_id: Int = data[TEAM_CITY_KEY]!!["value"] as Int
        val arena_id: Int = data[TEAM_ARENA_KEY]!!["value"] as Int
        arena_sender["city_id"] = city_id
        arena_sender["arena_id"] = arena_id
        data[TEAM_ARENA_KEY]!!["sender"] = arena_sender
    }

    fun setDaysSender() {
        data[TEAM_DAYS_KEY]!!["sender"] = data[TEAM_DAYS_KEY]!!["value"]!!
    }

    fun setDegreeSender() {
        data[TEAM_DEGREE_KEY]!!["sender"] = data[TEAM_DEGREE_KEY]!!["value"]!!
    }

    fun setPlayStartTimeSender() {
        var res: MutableMap<String, Any> = mutableMapOf()
        var time: String = data[TEAM_PLAY_START_KEY]!!["value"] as String
        time = time.noSec()
        res["type"] = SELECT_TIME_TYPE.play_start
        res["time"] = time
        data[TEAM_PLAY_START_KEY]!!["sender"] = res
    }
    fun setPlayEndTimeSender() {
        var res: MutableMap<String, Any> = mutableMapOf()
        var time: String = data[TEAM_PLAY_END_KEY]!!["value"] as String
        time = time.noSec()
        res["type"] = SELECT_TIME_TYPE.play_end
        res["time"] = time
        data[TEAM_PLAY_END_KEY]!!["sender"] = res
    }
    fun setTempContentSender() {
        var res: MutableMap<String, Any> = mutableMapOf()
        val text: String = data[TEAM_TEMP_CONTENT_KEY]!!["value"] as String
        res["text"] = text
        res["type"] = TEXT_INPUT_TYPE.temp_play
        data[TEAM_TEMP_CONTENT_KEY]!!["sender"] = res
    }
    fun setChargeSender() {
        var res: MutableMap<String, Any> = mutableMapOf()
        val text: String = data[TEAM_CHARGE_KEY]!!["value"] as String
        res["text"] = text
        res["type"] = TEXT_INPUT_TYPE.charge
        data[TEAM_CHARGE_KEY]!!["sender"] = res
    }
    fun setContentSender() {
        var res: MutableMap<String, Any> = mutableMapOf()
        val text: String = data[TEAM_CONTENT_KEY]!!["value"] as String
        res["text"] = text
        res["type"] = TEXT_INPUT_TYPE.team
        data[TEAM_CONTENT_KEY]!!["sender"] = res
    }
}

enum class DEGREE(val value: String) {
    new("新手"), soso("普通"), high("高手");
    companion object {
        fun from(findValue: String): DEGREE = DEGREE.values().first { it.value == findValue }
    }
}

enum class SELECT_TIME_TYPE(val value: Int) {
    play_start(0), play_end(1);
}

enum class TEXT_INPUT_TYPE(val value: Int) {
    temp_play(0), charge(1), team(2)
}