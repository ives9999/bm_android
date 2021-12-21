package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import com.google.gson.JsonParseException
import com.sportpassword.bm.Adapters.SignupAdapter
import com.sportpassword.bm.Data.ShowRow
import com.sportpassword.bm.Data.SignupRow
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.Models.TeamTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.jsonToModel
import com.sportpassword.bm.Utilities.noSec
import com.sportpassword.bm.Utilities.toDate
import com.sportpassword.bm.Utilities.toMyString
import kotlinx.android.synthetic.main.activity_show_course_vc.*
import kotlinx.android.synthetic.main.activity_show_team_vc.*
import kotlinx.android.synthetic.main.activity_show_team_vc.refresh
import kotlinx.android.synthetic.main.activity_show_team_vc.signupButton
import kotlinx.android.synthetic.main.activity_show_team_vc.signupDateLbl
import kotlinx.android.synthetic.main.activity_show_team_vc.signupTableView
import java.util.*
import kotlin.reflect.full.memberProperties

class ShowTeamVC: ShowVC() {

    var myTable: TeamTable? = null

    var isTempPlay: Boolean = true

    lateinit var signupAdapter: SignupAdapter
    var signupRows: ArrayList<SignupRow> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_show_team_vc)

        dataService = TeamService

        refreshLayout = refresh
        setRefreshListener()

//        initAdapter()
        super.onCreate(savedInstanceState)

//        tableRowKeys = mutableListOf("arena","interval_show","ball","leader","mobile_show","fb","youtube","website","email","pv","created_at_show")
//        tableRows = hashMapOf(
//            "arena" to hashMapOf("icon" to "arena","title" to "球館","content" to ""),
//            "interval_show" to hashMapOf("icon" to "clock","title" to "時段","content" to ""),
//            "ball" to hashMapOf("icon" to "ball","title" to "球種","content" to ""),
//            "leader" to hashMapOf("icon" to "member1","title" to "隊長","content" to ""),
//            "mobile_show" to hashMapOf("icon" to "mobile","title" to "行動電話","content" to ""),
//            "fb" to  hashMapOf("icon" to "fb","title" to "FB","content" to ""),
//            "youtube" to hashMapOf("icon" to "youtube","title" to "Youtube","content" to ""),
//            "website" to hashMapOf("icon" to "website","title" to "網站","content" to ""),
//            "email" to hashMapOf("icon" to "email1","title" to "EMail","content" to ""),
//            "pv" to hashMapOf("icon" to "pv","title" to "瀏覽數","content" to ""),
//            "created_at_show" to hashMapOf("icon" to "calendar","title" to "建立日期","content" to "")
//        )

        signupAdapter = SignupAdapter(this)
        signupTableView.adapter = signupAdapter

        init()
        refresh()
    }

    override fun init() {
        showRows.addAll(arrayListOf(
            ShowRow("arena", "arena1", "球館"),
            ShowRow("interval_show", "clock", "時段"),
            ShowRow("ball", "ball", "球種"),
            ShowRow("degree", "degree", "程度"),
            ShowRow("leader", "group", "管理者"),
            ShowRow("mobile_show", "mobile", "行動電話"),
            ShowRow("line", "line", "line"),
            ShowRow("fb", "fb", "FB"),
            ShowRow("youtube", "youtube", "Youtube"),
            ShowRow("website", "website", "網站"),
            ShowRow("email", "email1", "EMail"),
            ShowRow("pv", "pv", "瀏覽數"),
            ShowRow("created_at_show", "date", "建立日期")
        ))

        super.init()
    }

    override fun genericTable() {
        //println(dataService.jsonString)
        try {
            table = jsonToModel<TeamTable>(dataService.jsonString)
        } catch (e: JsonParseException) {
            warning(e.localizedMessage!!)
            //println(e.localizedMessage)
        }
        if (table != null) {
            myTable = table as TeamTable
            myTable!!.filterRow()
        } else {
            warning("解析伺服器所傳的字串失敗，請洽管理員")
        }
    }

    override fun setData() {

        if (myTable != null) {
            setMainData(myTable!!)
            setSignupData()
        }
    }

    override fun setMainData(table: Table) {
        for (showRow in showRows) {
            val key: String = showRow.key
            val kc = table::class
            kc.memberProperties.forEach {
                if (key == it.name) {
                    var value = it.getter.call(table).toString()
                    if (value == "null") value = ""
                    if (value == "-1") value = ""
                    showRow.show = value

                    if (key == "arena") {
                        if (myTable!!.arena != null) {
                            showRow.show = myTable!!.arena!!.name
                        } else {
                            showRow.show = "未提供"
                        }
                    }
                }
            }
        }

//        val items = generateMainItem()
//        adapter.update(items)

    }

    fun setSignupData() {

        isTempPlayOnline()

        if (!isTempPlay) {
            signupDataLbl.text = "目前球隊不開放臨打"
            deadlineLbl.visibility = View.GONE
            signupTableView.visibility = View.GONE
        } else {
            if (myTable != null && myTable!!.signupDate != null) {
                signupDataLbl.text = "報名資料"
                signupDateLbl.text =
                    "下次臨打時間：" + myTable!!.signupDate!!.date + " " + myTable!!.interval_show
                deadlineLbl.text = "報名截止時間：" + myTable!!.signupDate!!.deadline.noSec()
            }

        }

        if (myTable!!.people_limit == 0) {
            signupButton.visibility = View.GONE
        } else {
            for (i in 0..myTable!!.people_limit - 1) {
                var name = ""
                if (myTable!!.signupNormalTables.count() > i) {
                    val tmp = myTable!!.signupNormalTables[i].member_name?.let {
                        name = it
                    }
                }
                val signupRow: SignupRow = SignupRow((i+1).toString()+".", name)
                signupRows.add(signupRow)
            }
            if (myTable!!.signupStandbyTables.count() > 0) {
                for (i in 0..myTable!!.signupStandbyTables.count() - 1) {
                    var name = ""
                    val tmp = myTable!!.signupStandbyTables[i].member_name?.let {
                        name = it
                    }
                    val signupRow: SignupRow = SignupRow("候補" + (i+1).toString()+".", name)
                    signupRows.add(signupRow)
                }
            }

            signupAdapter.rows = signupRows
            signupAdapter.notifyDataSetChanged()
        }

        if (myTable!!.isSignup) {
            signupButton.setText("取消報名")
        } else {
            val count = myTable!!.signupNormalTables.size
            if (count >= myTable!!.people_limit) {
                signupButton.setText("候補")
            } else {
                signupButton.setText("報名")
            }
        }
    }

    fun isTempPlayOnline() {

        //1.如果臨打狀態是關閉，關閉臨打
        if (myTable!!.temp_status == "offline") {
            isTempPlay = false
        }

        //2.如果沒有設定臨打日期，關閉臨打
        if (myTable!!.signupDate != null) {
            val temp_date_string: String = myTable!!.signupDate!!.date + " 00:00:00"

            //3.如果臨打日期超過現在的日期，關閉臨打
            var temp_date: Date = Date()
            temp_date_string.toDate("yyyy-MM-dd HH:mm:ss")?.let {
                temp_date = it
            }

            val today_string: String = Date().toMyString("yyyy-MM-dd") + " 23:59:59"
            var today: Date = Date()
            today_string.toDate("yyyy-MM-dd HH:mm:ss")?.let {
                today = it
            }

            if (temp_date.before(today)) {
                isTempPlay = false
            }
        }

        //3.如果管理者設定報名臨打名額是0，關閉臨打
        if (myTable!!.people_limit == 0) {
            isTempPlay = false
        }
    }

//    override fun didSelectRowAt(view: View, position: Int) {
//
//    }

}