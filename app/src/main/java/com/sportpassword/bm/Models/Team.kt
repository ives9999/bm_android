package com.sportpassword.bm.Models

import android.text.InputType
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member

/**
 * Created by ives on 2018/2/14.
 */
class Team(id: Int, name: String, token: String, featured_path: String, vimeo: String="", youtube: String=""): SuperData(id, name, token, featured_path, vimeo, youtube) {

    var TO_CITY: String = "toCity"
    var TO_ARENA: String = "toArena"
    var TO_DAY: String = "toDay"
    var TO_SELECT_TIME: String = "toSelectTime"
    var TO_TEXT_INPUT: String = "toTextInput"
    var TO_SELECT_DEGREE: String = "toSelectDegree"
    var lists: ArrayList<Map<String, Map<String, Any>>> = arrayListOf()
    var temp_play_data: MutableMap<String, MutableMap<String, Any>> = mutableMapOf()

    override var sections: ArrayList<String> = arrayListOf("", "聯絡資訊", "所在地", "打球時間", "臨打說明", "其他說明")
    override var rows: ArrayList<ArrayList<String>> = arrayListOf(
            arrayListOf(NAME_KEY),
            arrayListOf(TEAM_LEADER_KEY,MOBILE_KEY,EMAIL_KEY),
            arrayListOf(CITY_KEY, ARENA_KEY),
            arrayListOf(TEAM_DAYS_KEY,TEAM_PLAY_START_KEY,TEAM_PLAY_END_KEY),
            arrayListOf(TEAM_TEMP_FEE_M_KEY,TEAM_TEMP_FEE_F_KEY, TEAM_TEMP_CONTENT_KEY),
            arrayListOf(TEAM_BALL_KEY,TEAM_DEGREE_KEY,CHARGE_KEY,CONTENT_KEY)
    )
    
    init {
        dataReset()
        tempPlayDataReset()
    }


    //atype: more or none
    override fun dataReset() {
        val _data = mutableMapOf<String, MutableMap<String, Any>>(
                ID_KEY to mutableMapOf("ch" to "編號","vtype" to "Int","value" to -1),
                CHANNEL_KEY to mutableMapOf("ch" to "頻道","vtype" to "String","value" to "","submit" to false,"show" to ""),
                WEBSITE_KEY to mutableMapOf("ch" to "網站","vtype" to "String","value" to "","submit" to false,"show" to ""),
                FB_KEY to mutableMapOf("ch" to "FB","vtype" to "String","value" to "","submit" to false,"show" to ""),
                YOUTUBE_KEY to mutableMapOf("ch" to "youtube","vtype" to "String","value" to "","submit" to false,"show" to ""),
                SLUG_KEY to mutableMapOf("ch" to "插槽","vtype" to "String","value" to "","submit" to true,"show" to ""),
                MANAGER_ID_KEY to mutableMapOf("ch" to "管理者編號","vtype" to "Int","value" to -1,"submit" to false,"show" to ""),
                TEAM_TEMP_QUANTITY_KEY to mutableMapOf("ch" to "臨打人數缺額","vtype" to "Int","value" to -1,"submit" to false,"show" to ""),
                TEAM_TEMP_STATUS_KEY to mutableMapOf("ch" to "臨打狀態","vtype" to "String","value" to "","submit" to false,"show" to ""),
                PV_KEY to mutableMapOf("ch" to "瀏覽數","vtype" to "Int","value" to -1,"submit" to false,"show" to ""),
                TOKEN_KEY to mutableMapOf("ch" to "球隊token","vtype" to "String","value" to "","submit" to false,"show" to ""),
                CREATED_ID_KEY to mutableMapOf("ch" to "建立者","vtype" to "Int","value" to -1,"submit" to false,"show" to ""),
                CREATED_AT_KEY to mutableMapOf("ch" to "建立時間","vtype" to "String","value" to "","submit" to false,"show" to ""),
                UPDATED_AT_KEY to mutableMapOf("ch" to "最後一次修改時間","vtype" to "String","value" to "","submit" to false,"show" to ""),
                THUMB_KEY to mutableMapOf("ch" to "代表圖","vtype" to "String","value" to "","submit" to false,"show" to ""),
                TEAM_NEAR_DATE_KEY to mutableMapOf("ch" to "下次臨打日期","vtype" to "String","value" to "","submit" to false,"show" to ""),
                TEAM_TEMP_SIGNUP_KEY to mutableMapOf("ch" to "已報名人數","vtype" to "String","value" to "","submit" to false,"show" to ""),
                NAME_KEY to mutableMapOf("ch" to "名稱","vtype" to "String","value" to "","submit" to true,"atype" to none,"show" to "","text_field" to true,"keyboardType" to defaultPad),
                TEAM_LEADER_KEY to mutableMapOf("ch" to "聯絡人","vtype" to "String","value" to "","submit" to true,"atype" to none,"show" to "","text_field" to true,"keyboardType" to defaultPad),
                MOBILE_KEY to mutableMapOf("ch" to "電話","vtype" to "String","value" to "","submit" to true,"atype" to none,"show" to "","text_field" to true,"keyboardType" to phonePad),
                EMAIL_KEY to mutableMapOf("ch" to "EMail","vtype" to "String","value" to "","submit" to true,"atype" to none,"show" to "","text_field" to true,"keyboardType" to emailPad),
                TEAM_PLAY_START_KEY to mutableMapOf("ch" to "開始時間","vtype" to "String","value" to "","submit" to true,"atype" to more,"segue" to TO_SELECT_TIME,"sender" to mutableMapOf<String, Any>(),"show" to ""),
                TEAM_PLAY_END_KEY to mutableMapOf("ch" to "結束時間","vtype" to "String","value" to "","submit" to true,"atype" to more,"segue" to TO_SELECT_TIME,"sender" to mutableMapOf<String, Any>(),"show" to ""),
                TEAM_INTERVAL_KEY to mutableMapOf("ch" to "打球時段","vtype" to "String","value" to "","submit" to false,"atype" to none,"segue" to TO_SELECT_TIME,"sender" to mutableMapOf<String, Any>(),"show" to ""),
                TEAM_BALL_KEY to mutableMapOf("ch" to "使用球種","vtype" to "String","value" to "","submit" to true,"atype" to none,"show" to "","text_field" to true,"keyboardType" to defaultPad),
                TEAM_DEGREE_KEY to mutableMapOf("ch" to "球隊程度","vtype" to "array","value" to mutableListOf<String>(),"submit" to true,"atype" to more,"segue" to TO_SELECT_DEGREE,"sender" to arrayListOf<DEGREE>(),"show" to ""),
                CHARGE_KEY to mutableMapOf("ch" to "收費說明","vtype" to "String","value" to "","submit" to true,"atype" to more,"segue" to TO_TEXT_INPUT,"sender" to mutableMapOf<String, Any>(),"show" to ""),
                CONTENT_KEY to mutableMapOf("ch" to "球隊說明","vtype" to "String","value" to "","submit" to true,"atype" to more,"segue" to TO_TEXT_INPUT,"sender" to mutableMapOf<String, Any>(),"show" to ""),
                TEAM_TEMP_FEE_M_KEY to mutableMapOf("ch" to "臨打費用：男","vtype" to "Int","value" to -1,"submit" to true,"atype" to none,"show" to "","text_field" to true,"keyboardType" to numberPad),
                TEAM_TEMP_FEE_F_KEY to mutableMapOf("ch" to "臨打費用：女","vtype" to "Int","value" to -1,"submit" to true,"atype" to none,"show" to "","text_field" to true,"keyboardType" to numberPad),
                TEAM_TEMP_CONTENT_KEY to mutableMapOf("ch" to "臨打說明","vtype" to "String","value" to "","submit" to true,"atype" to more,"segue" to TO_TEXT_INPUT,"sender" to mutableMapOf<String, Any>(),"show" to ""),
                CITY_KEY to mutableMapOf("ch" to "區域","vtype" to "array","value" to 0,"submit" to true,"atype" to more,"segue" to TO_CITY,"sender" to 0,"show" to ""),
                ARENA_KEY to mutableMapOf("ch" to "球館","vtype" to "array","value" to 0,"submit" to true,"atype" to more,"segue" to TO_ARENA,"sender" to mutableMapOf<String, Int>(),"show" to ""),
                TEAM_DAYS_KEY to mutableMapOf("ch" to "星期幾","vtype" to "array","value" to mutableListOf<Int>(),"submit" to true,"atype" to more,"segue" to TO_DAY,"sender" to mutableListOf<Int>(),"show" to ""),
                FEATURED_KEY to mutableMapOf("ch" to "代表圖","vtype" to "String","value" to "","path" to "","submit" to false,"show" to "")
        )
        data = _data
        for ((key, value) in data) {
            data[key]!!["change"] = false
            data[key]!!["key"] = key
        }
    }
    fun tempPlayDataReset() {
        temp_play_data = mutableMapOf(
                ID_KEY to mutableMapOf("ch" to "編號","vtype" to "Int","value" to -1,"show" to "","submit" to false,"key" to ID_KEY),
                NAME_KEY to mutableMapOf("ch" to "名稱","vtype" to "String","value" to "","submit" to false,"show" to "","key" to NAME_KEY),
                TEAM_TEMP_STATUS_KEY to mutableMapOf("ch" to "臨打狀態","vtype" to "String","value" to "off","show" to "off","submit" to true,"change" to false,"key" to TEAM_TEMP_STATUS_KEY),
                TEAM_TEMP_QUANTITY_KEY to mutableMapOf("ch" to "臨打人數","vtype" to "Int","value" to -1,"show" to "","hidden" to true,"submit" to true,"change" to false,"key" to TEAM_TEMP_QUANTITY_KEY)
        )
    }

    fun runTestData() {
        val testData = mapOf<String, Any>(
                NAME_KEY to "快樂羽球隊",
                TEAM_LEADER_KEY to "孫志煌",
                MOBILE_KEY to "0911299994",
                EMAIL_KEY to "ives@housetube.tw",
                TEAM_TEMP_FEE_M_KEY to 150,
                TEAM_TEMP_FEE_F_KEY to 100,
                TEAM_BALL_KEY to "RSL 4",
                CONTENT_KEY to "請勿報名沒有來，列入黑名單",
                CHARGE_KEY to "一季3600含球",
                TEAM_TEMP_CONTENT_KEY to "歡迎加入",
                TEAM_PLAY_START_KEY to "16:00",
                TEAM_PLAY_END_KEY to "18:00",
                TEAM_DEGREE_KEY to arrayListOf(DEGREE.high, DEGREE.soso),
                TEAM_DAYS_KEY to arrayListOf(2, 4),
                CITY_KEY to City(218, "台南"),
                ARENA_KEY to Arena(10, "全穎羽球館"),
                CREATED_ID_KEY to 1
        )
        if (testData.size > 0) {
            for ((key1, value) in testData) {
                if (key1 == CITY_KEY) {
                    val city: City = value as City
                    data[key1]!!["value"] = city.id
                    data[key1]!!["show"] = city.name
                } else if (key1 == ARENA_KEY) {
                    val arena: Arena = value as Arena
                    data[key1]!!["value"] = arena.id
                    data[key1]!!["show"] = arena.title
                } else {
                    for ((key2, row) in data) {
                        if (key1 == key2) {
                            data[key2]!!["value"] = value
                            val vtype: String = row["vtype"] as String
                            if (vtype != "array") {
                                data[key2]!!["show"] = value
                            }
                        }
                    }
                }
                data[key1]!!["change"] = true
            }
            updateDays(testData[TEAM_DAYS_KEY] as ArrayList<Int>)
            updateDegree(testData[TEAM_DEGREE_KEY] as ArrayList<DEGREE>)
        }
    }

    override fun updateCity(city: City?) {
        if (city != null) {
            data[CITY_KEY]!!["value"] = city.id
            data[CITY_KEY]!!["show"] = city.name
            data[CITY_KEY]!!["sender"] = city.id
            setArenaSender()
        } else {
            data[CITY_KEY]!!["value"] = 0
            data[CITY_KEY]!!["show"] = ""
            data[CITY_KEY]!!["sender"] = 0
        }
    }

    override fun updateArena(arena: Arena?) {
        if (arena != null) {
            data[ARENA_KEY]!!["value"] = arena.id
            data[ARENA_KEY]!!["show"] = arena.title
            setArenaSender()
        } else {
            data[ARENA_KEY]!!["value"] = 0
            data[ARENA_KEY]!!["show"] = ""
        }
    }

    override fun updateDays(days: ArrayList<Int>?) {
        if (days != null) {
            data[TEAM_DAYS_KEY]!!["value"] = days
        } else {
            data[TEAM_DAYS_KEY]!!["value"] = mutableListOf<Int>()

        }
        daysShow()
        setDaysSender()
    }

    override fun updateDegree(degrees: ArrayList<DEGREE>?) {
        if (degrees != null && degrees.size > 0) {
            var res: MutableList<String> = mutableListOf()
            degrees.forEach {
                res.add(it.toString())
            }
            data[TEAM_DEGREE_KEY]!!["value"] = res
        } else {
            data[TEAM_DEGREE_KEY]!!["value"] = mutableListOf<String>()
        }
        degreeShow()
        setDegreeSender()
    }

    override fun updatePlayStartTime(time: String?) {
        if (time != null) {
            data[TEAM_PLAY_START_KEY]!!["value"] = time
            data[TEAM_PLAY_START_KEY]!!["show"] = time.noSec()
        } else {
            data[TEAM_PLAY_START_KEY]!!["value"] = ""
            data[TEAM_PLAY_START_KEY]!!["show"] = ""
        }
        setPlayStartTimeSender()
    }
    override fun updatePlayEndTime(time: String?) {
        if (time != null) {
            data[TEAM_PLAY_END_KEY]!!["value"] = time
            data[TEAM_PLAY_END_KEY]!!["show"] = time.noSec()
        } else {
            data[TEAM_PLAY_END_KEY]!!["value"] = ""
            data[TEAM_PLAY_END_KEY]!!["show"] = ""
        }
        setPlayEndTimeSender()
    }
    fun updateInterval(_startTime: String? = null, _endTime: String? = null) {
        var startTime = _startTime
        if (_startTime == null) {
            startTime = data[TEAM_PLAY_START_KEY]!!["show"] as String
        }
        var endTime = _endTime
        if (_endTime == null) {
            endTime = data[TEAM_PLAY_END_KEY]!!["show"] as String
        }
        var tmp: String = ""
        if (startTime != null && endTime != null) {
            tmp = startTime + " ~ " + endTime
        }
        data[TEAM_INTERVAL_KEY]!!["show"] = tmp
    }
    override fun updateTempContent(content: String?) {
        var _content = ""
        if (content != null) {
            _content = content
        }
        data[TEAM_TEMP_CONTENT_KEY]!!["value"] = _content
        tempContentShow()
        setTempContentSender()
    }
    override fun updateCharge(content: String?) {
        var _content = ""
        if (content != null) {
            _content = content
        }
        data[CHARGE_KEY]!!["value"] = _content
        chargeShow()
        setChargeSender()
    }
    override fun updateContent(content: String?) {
        var _content = ""
        if (content != null) {
            _content = content
        }
        data[CONTENT_KEY]!!["value"] = _content
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
            if (data[TEAM_NEAR_DATE_KEY]!!.containsKey("value1")) {
                data[TEAM_NEAR_DATE_KEY]!!["value1"] = n2
                nn2 = n2!!
            }
        } else {
            if (data[TEAM_NEAR_DATE_KEY]!!.containsKey("value1")) {
                nn2 = (data[TEAM_NEAR_DATE_KEY]!!["value1"] as String)
            }
        }
        val n: String = nn1 + "(" + nn2 + ")"
        data[TEAM_NEAR_DATE_KEY]!!["show"] = n
    }

    fun daysShow() {
        val row: Map<String, Any> = data[TEAM_DAYS_KEY]!!
        val days = row["value"] as ArrayList<Int>
        var show = ""
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
            show = res.joinToString(", ")
        }
        data[TEAM_DAYS_KEY]!!["show"] = show
    }

    fun degreeShow() {
        val degrees: ArrayList<String> = data[TEAM_DEGREE_KEY]!!["value"] as ArrayList<String>
        var res: ArrayList<String> = arrayListOf()
        for (i in 0..degrees.size-1) {
            val degree = degrees[i]
            try {
                val type: DEGREE = DEGREE.valueOf(degree)
                val text: String = type.value
                res.add(text)
            } catch (e: Exception) {
                println("parse degree exception: " + e.localizedMessage)
            }
        }
        if (res.size > 0) {
            data[TEAM_DEGREE_KEY]!!["show"] = res.joinToString(", ")
        } else {
            data[TEAM_DEGREE_KEY]!!["show"] = ""
        }
    }
    fun tempContentShow(length: Int=12) {
        var text: String = data[TEAM_TEMP_CONTENT_KEY]!!["value"] as String
        text = text.truncate(length)
        data[TEAM_TEMP_CONTENT_KEY]!!["show"] = text
    }
    fun chargeShow(length: Int=12) {
        var text: String = data[CHARGE_KEY]!!["value"] as String
        text = text.truncate(length)
        data[CHARGE_KEY]!!["show"] = text
    }
    fun contentShow(length: Int=12) {
        var text: String = data[CONTENT_KEY]!!["value"] as String
        text = text.truncate(length)
        data[CONTENT_KEY]!!["show"] = text
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
        val city_id: Int = data[CITY_KEY]!!["value"] as Int
        val arena_id: Int = data[ARENA_KEY]!!["value"] as Int
        arena_sender["city_id"] = city_id
        arena_sender["arena_id"] = arena_id
        data[ARENA_KEY]!!["sender"] = arena_sender
    }

    fun setDaysSender() {
        data[TEAM_DAYS_KEY]!!["sender"] = data[TEAM_DAYS_KEY]!!["value"]!!
    }

    fun setDegreeSender() {
        val degrees: ArrayList<String> = data[TEAM_DEGREE_KEY]!!["value"]!! as ArrayList<String>
        var res: ArrayList<DEGREE> = arrayListOf()
        degrees.forEach {
            res.add(DEGREE.fromEnglish(it))
        }
        data[TEAM_DEGREE_KEY]!!["sender"] = res
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
        val text: String = data[CHARGE_KEY]!!["value"] as String
        res["text"] = text
        res["type"] = TEXT_INPUT_TYPE.charge
        data[CHARGE_KEY]!!["sender"] = res
    }
    fun setContentSender() {
        var res: MutableMap<String, Any> = mutableMapOf()
        val text: String = data[CONTENT_KEY]!!["value"] as String
        res["text"] = text
        res["type"] = TEXT_INPUT_TYPE.team
        data[CONTENT_KEY]!!["sender"] = res
    }

    fun makeTempPlaySubmitArr(): MutableMap<String, Any> {
        var isAnyOneChage: Boolean = false
        var res: MutableMap<String, Any> = mutableMapOf()
        for ((key, row) in temp_play_data) {
            val isSubmit: Boolean = row["submit"]!! as Boolean
            var isChange: Boolean = false
            if (row.containsKey("change")) {
                isChange = row["change"]!! as Boolean
            }
            if (isSubmit && isChange) {
                res[key] = row["value"]!!
                if (!isAnyOneChage) isAnyOneChage = true
            }
        }
        if (!isAnyOneChage) {
            return res
        }
        res[ID_KEY] = temp_play_data[ID_KEY]!!["value"]!!
        return res
    }
}

enum class SELECT_TIME_TYPE(val value: Int) {
    play_start(0), play_end(1);
}

enum class TEXT_INPUT_TYPE(val value: Int) {
    temp_play(0), charge(1), team(2)
}