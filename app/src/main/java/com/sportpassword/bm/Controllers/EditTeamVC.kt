package com.sportpassword.bm.Controllers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.JsonParseException
import com.sportpassword.bm.Data.OneRow
import com.sportpassword.bm.Models.*
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.CourseService
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.mask.*
import java.util.*
import kotlin.collections.ArrayList

class EditTeamVC : EditVC() {

    var myTable: TeamTable? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        able_type = "team"
        dataService = TeamService

        setContentView(R.layout.activity_edit_team_vc)

        if (token == null|| token.isEmpty()) {
//            myTable = TeamTable()
//            myTable!!.arena = ArenaTable()
//            myTable!!.name = "測試與球隊"
//            myTable!!.city_id = 218
//            myTable!!.arena_id = 6
//            val arena: ArenaTable = ArenaTable()
//            arena.name = "艾婕"
//            myTable!!.arena = arena
//            val weekdays1: Team_WeekdaysTable = Team_WeekdaysTable()
//            weekdays1.weekday = 2
//            val weekdays2: Team_WeekdaysTable = Team_WeekdaysTable()
//            weekdays2.weekday = 4
//            myTable!!.weekdays = arrayListOf<Team_WeekdaysTable>(weekdays1, weekdays2)
//            myTable!!.play_start = "19:00:00"
//            myTable!!.play_end = "21:00:00"
//            myTable!!.degree = "high,soso"
//            myTable!!.ball = "RSL4號"
//            myTable!!.temp_fee_M = 300
//            myTable!!.temp_fee_F = 200
//            myTable!!.manager_id = 1
//            myTable!!.manager_nickname = "xxx"
//            myTable!!.line = "rich@gmail.com"
//            myTable!!.mobile = "0920123456"
//            myTable!!.email = "john@housetube.tw"
//            myTable!!.youtube = "youtube"
//            myTable!!.fb = "fb"
//            myTable!!.charge = "收費詳細說明"
//            myTable!!.content = "這是內容說明"
//            myTable!!.filterRow()
        }
        super.onCreate(savedInstanceState)

        setMyTitle("新增羽球隊")
    }

    override fun initData() {

        if (myTable == null) {
            myTable = TeamTable()
            myTable!!.arena = ArenaTable()
        }

        val rows: ArrayList<OneRow> = arrayListOf()
        val manager_id: String = (myTable!!.manager_id <= 0) then { "" } ?: myTable!!.manager_id.toString()
        var row: OneRow = OneRow(
            "管理員",
            manager_id,
            myTable!!.manager_nickname,
            MANAGER_ID_KEY,
            "more"
        )
        row.prompt = "如果新增者就是管理者，就不用填寫此項目"
        row.token = myTable!!.manager_token
        rows.add(row)
        row = OneRow(
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
            ARENA_KEY,
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
            WEEKDAYS_KEY,
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
            "場地",
            myTable!!.block.toString(),
            myTable!!.block_show,
            BLOCK_KEY,
            "textField",
            KEYBOARD.numberPad
        )
        rows.add(row)
        row = OneRow(
            "球種",
            myTable!!.ball,
            myTable!!.ball,
            TEAM_BALL_KEY,
            "textField",
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

        val people_limit: String = (myTable!!.people_limit > 0) then { myTable!!.people_limit.toString()} ?: ""
        row = OneRow(
            "臨打名額",
            people_limit,
            people_limit,
            PEOPLE_LIMIT_KEY,
            "textField",
            KEYBOARD.numberPad
        )
        rows.add(row)

        val temp_fee_M: String = (myTable!!.temp_fee_M >= 0) then { myTable!!.temp_fee_M.toString() } ?: ""
        row = OneRow(
            "臨打費用-男",
            temp_fee_M,
            temp_fee_M,
            TEAM_TEMP_FEE_M_KEY,
            "textField",
            KEYBOARD.numberPad,
            "300"
        )
        rows.add(row)

        val temp_fee_F: String = (myTable!!.temp_fee_F >= 0) then { myTable!!.temp_fee_F.toString() } ?: ""
        row = OneRow(
            "臨打費用-女",
            temp_fee_F,
            temp_fee_F,
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
            CONTENT_KEY,
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
            runOnUiThread {
                warning("解析伺服器所傳的字串失敗，請洽管理員")
            }
        }
    }

    override fun cellSwitchChanged(sectionIdx: Int, rowIdx: Int, b: Boolean) {

        val row: OneRow = getOneRowFromIdx(sectionIdx, rowIdx)
        row.value = b then { "online" } ?: "offline"
        row.show = b then { "上線" } ?: "下線"
    }

    override fun submitValidate() {

        val row: OneRow = getOneRowFromKey(TEAM_TEMP_DATE_KEY)
        val temp_date_string: String = row.value
        val temp_date: Date? = temp_date_string.toDate()
        if (temp_date != null) {
//            if (temp_date.isSmallerThan(Date()) {
//                msg = "臨打日期必須在明天之後\n"
//            }
        }
    }

    fun signupButtonPressed(view: View) {

        params.clear()
        params["cat_id"] = "21"
        super.submit(view)
    }
}