package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.google.gson.JsonParseException
import com.sportpassword.bm.Data.OneRow
import com.sportpassword.bm.Models.CourseTable
import com.sportpassword.bm.Models.TeamTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.*

class EditTeamVC : EditVC() {

    var myTable: TeamTable? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        able_type = "team"
        dataService = TeamService

        setContentView(R.layout.activity_edit_team_vc)
        super.onCreate(savedInstanceState)
    }

    override fun initData() {

        if (myTable == null) {
            myTable = TeamTable()
        }

        val rows: ArrayList<OneRow> = arrayListOf()
        var row = OneRow(
            "名稱",
            myTable!!.name,
            myTable!!.name,
            NAME_KEY,
            "textField",
            KEYBOARD.default,
            "羽球密碼羽球隊",
            "",
            true
        )
        row.msg = "球隊名稱沒有填寫"
        rows.add(row)
        row = OneRow(
            "縣市",
            myTable!!.city_id.toString(),
            myTable!!.city_show,
            CITY_KEY,
            "more",
            KEYBOARD.default,
            "",
            "",
            true
        )
        row.msg = "沒有選擇縣市"
        rows.add(row)
        row = OneRow(
            "球館",
            myTable!!.arena_id.toString(),
            myTable!!.arena!!.name,
            CITY_KEY,
            "more",
            KEYBOARD.default,
            "",
            "",
            true
        )
        row.msg = "沒有選擇球館"
        rows.add(row)
        var section = makeSectionRow("基本資料", "general", rows, true)
        oneSections.add(section)

        rows.clear()
        row = OneRow(
            "球隊狀態",
            myTable!!.status,
            myTable!!.status_show,
            STATUS_KEY,
            "switch"
        )
        rows.add(row)
        var weekdays: Int = 0
        for (weekday in myTable!!.weekdays) {
            val n: Int = Math.pow(2.0, weekday.weekday.toDouble()).toInt()
            weekdays = weekdays or n
        }
        row = OneRow(
            "星期幾",
            weekdays.toString(),
            myTable!!.weekdays_show,
            WEEKDAY_KEY,
            "more",
            KEYBOARD.default,
            "請選擇星期幾打球",
            "",
            true
        )
        row.msg = "沒有選擇星期幾"
        rows.add(row)
        row = OneRow(
            "開始時間",
            myTable!!.play_start,
            myTable!!.play_start_show,
            TEAM_PLAY_START_KEY,
            "more",
            KEYBOARD.default,
            "請選擇球隊打球開始時間",
            "",
            true
        )
        row.msg = "沒有選擇開始時間"
        rows.add(row)
        row = OneRow(
            "結束時間",
            myTable!!.play_end,
            myTable!!.play_end_show,
            TEAM_PLAY_END_KEY,
            "more",
            KEYBOARD.default,
            "請選擇球隊打球結束時間",
            "",
            true
        )
        row.msg = "沒有選擇結束時間"
        rows.add(row)
        row = OneRow(
            "程度",
            myTable!!.degree,
            myTable!!.degree_show,
            DEGREE_KEY,
            "more"
        )
        rows.add(row)
        row = OneRow(
            "球種",
            myTable!!.ball,
            myTable!!.ball,
            TEAM_BALL_KEY,
            "more",
            KEYBOARD.default,
            "RSL4號球"
        )
        rows.add(row)
        section = makeSectionRow("打球資訊", "play", rows, true)
        oneSections.add(section)

        rows.clear()
        row = OneRow(
            "臨打狀態",
            myTable!!.temp_status,
            myTable!!.temp_status_show,
            TEAM_TEMP_STATUS_KEY,
            "switch"
        )
        rows.add(row)
        row = OneRow(
            "臨打日期",
            myTable!!.last_signup_date,
            myTable!!.last_signup_date,
            TEAM_TEMP_DATE_KEY,
            "more"
        )
        rows.add(row)
        row = OneRow(
            "臨打名額",
            myTable!!.temp_quantity.toString(),
            myTable!!.temp_quantity.toString(),
            TEAM_TEMP_QUANTITY_KEY,
            "textField",
            KEYBOARD.numberPad
        )
        rows.add(row)
        row = OneRow(
            "臨打費用-男",
            myTable!!.temp_fee_M.toString(),
            myTable!!.temp_fee_M.toString(),
            TEAM_TEMP_FEE_M_KEY,
            "textField",
            KEYBOARD.numberPad,
            "300"
        )
        rows.add(row)
        row = OneRow(
            "臨打費用-女",
            myTable!!.temp_fee_F.toString(),
            myTable!!.temp_fee_F.toString(),
            TEAM_TEMP_FEE_F_KEY,
            "textField",
            KEYBOARD.numberPad,
            "200"
        )
        rows.add(row)
        row = OneRow(
            "臨打說明",
            myTable!!.temp_content,
            myTable!!.temp_content,
            TEAM_TEMP_CONTENT_KEY,
            "more"
        )
        rows.add(row)
        section = makeSectionRow("臨打說明", "tempplay", rows, true)
        oneSections.add(section)

        rows.clear()
        row = OneRow(
            "隊長",
            myTable!!.manager_id.toString(),
            myTable!!.manager_nickname,
            MANAGER_ID_KEY,
            "more"
        )
        rows.add(row)
        row = OneRow(
            "line",
            myTable!!.line,
            myTable!!.line,
            LINE_KEY,
            "textField",
            KEYBOARD.default,
            "david221"
        )
        rows.add(row)
        row = OneRow(
            "行動電話",
            myTable!!.mobile,
            myTable!!.mobile,
            MOBILE_KEY,
            "textField",
            KEYBOARD.numberPad,
            "0939123456"
        )
        rows.add(row)
        row = OneRow(
            "EMail",
            myTable!!.email,
            myTable!!.email,
            EMAIL_KEY,
            "textField",
            KEYBOARD.emailAddress,
            "service@bm.com"
        )
        rows.add(row)
        row = OneRow(
            "youtube代碼",
            myTable!!.youtube,
            myTable!!.youtube,
            YOUTUBE_KEY,
            "textField",
            KEYBOARD.default
        )
        rows.add(row)
        row = OneRow(
            "FB",
            myTable!!.fb,
            myTable!!.fb,
            FB_KEY,
            "textField",
            KEYBOARD.default
        )
        rows.add(row)

        section = makeSectionRow("聯絡資料", "contact", rows, true)
        oneSections.add(section)

        rows.clear()
        row = OneRow(
            "收費詳細說明",
            myTable!!.charge,
            myTable!!.charge,
            CHARGE_KEY,
            "more"
        )
        rows.add(row)
        row = OneRow(
            "更多詳細說明",
            myTable!!.content,
            myTable!!.content,
            CHARGE_KEY,
            "more"
        )
        rows.add(row)
        section = makeSectionRow("詳細說明", "content", rows, true)
        oneSections.add(section)
    }

    override fun genericTable() {
        //println(dataService.jsonString)
        try {
            myTable = jsonToModel<TeamTable>(dataService.jsonString)
            if (myTable != null) {
                title = myTable!!.name
            }
        } catch (e: JsonParseException) {
            warning(e.localizedMessage)
            //println(e.localizedMessage)
        }
        if (myTable != null) {
            myTable!!.filterRow()
            table = myTable
        } else {
            warning("解析伺服器所傳的字串失敗，請洽管理員")
        }
    }

    override fun cellSwitchChanged(sectionIdx: Int, rowIdx: Int, b: Boolean) {

        val row: OneRow = getOneRowFromIdx(sectionIdx, rowIdx)
        row.value = b then { "online" } ?: "offline"
        row.show = b then { "上線" } ?: "下線"
    }


}