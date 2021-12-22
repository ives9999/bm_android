package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import com.google.gson.JsonParseException
import com.sportpassword.bm.Adapters.SignupAdapter
import com.sportpassword.bm.Data.ShowRow
import com.sportpassword.bm.Data.SignupRow
import com.sportpassword.bm.Models.SuccessTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.Models.TeamTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.*
import com.sportpassword.bm.member
import kotlinx.android.synthetic.main.activity_show_team_vc.*
import kotlinx.android.synthetic.main.activity_show_team_vc.refresh
import kotlinx.android.synthetic.main.activity_show_team_vc.signupButton
import kotlinx.android.synthetic.main.activity_show_team_vc.signupDateLbl
import kotlinx.android.synthetic.main.activity_show_team_vc.signupTableView
import java.util.*
import kotlin.reflect.full.memberProperties
import kotlinx.android.synthetic.main.mask.*

class ShowTeamVC: ShowVC() {

    var myTable: TeamTable? = null

    var isTempPlay: Boolean = true

    lateinit var signupAdapter: SignupAdapter

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

        showRows.addAll(arrayListOf(
            ShowRow("arena", "arena1", "球館", myTable!!.arena!!.name),
            ShowRow("interval_show", "clock", "時段", myTable!!.interval_show),
            ShowRow("ball", "ball", "球種", myTable!!.ball),
            ShowRow("degree", "degree", "程度", myTable!!.degree_show),
            ShowRow("leader", "group", "管理者", myTable!!.manager_nickname),
            ShowRow("mobile_show", "mobile", "行動電話", myTable!!.mobile_show),
            ShowRow("line", "line", "line", myTable!!.line),
            ShowRow("fb", "fb", "FB", myTable!!.fb),
            ShowRow("youtube", "youtube", "Youtube", myTable!!.youtube),
            ShowRow("website", "website", "網站", myTable!!.website),
            ShowRow("email", "email1", "EMail", myTable!!.email),
            ShowRow("pv", "pv", "瀏覽數", myTable!!.pv.toString()),
            ShowRow("created_at_show", "date", "建立日期", myTable!!.created_at_show)
        ))
//        for (showRow in showRows) {
//            val key: String = showRow.key
//            val kc = table::class
//            kc.memberProperties.forEach {
//                if (key == it.name) {
//                    var value = it.getter.call(table).toString()
//                    if (value == "null") value = ""
//                    if (value == "-1") value = ""
//                    showRow.show = value
//
//                    if (key == "arena") {
//                        if (myTable!!.arena != null) {
//                            showRow.show = myTable!!.arena!!.name
//                        } else {
//                            showRow.show = "未提供"
//                        }
//                    }
//                }
//            }
//        }

//        val items = generateMainItem()
//        adapter.update(items)

    }

    fun setSignupData() {

        isTempPlayOnline()

        if (!isTempPlay) {
            signupDataLbl.text = "目前球隊不開放臨打"
            signupDateLbl.visibility = View.INVISIBLE
            deadlineLbl.visibility = View.INVISIBLE
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
            val temp_date_string: String = myTable!!.signupDate!!.date + " 23:59:59"

            //3.如果臨打日期超過現在的日期，關閉臨打
            var temp_date: Date = Date()
            temp_date_string.toDate("yyyy-MM-dd HH:mm:ss")?.let {
                temp_date = it
            }

            val now_string: String = Date().toMyString("yyyy-MM-dd") + " 23:59:59"
            var now: Date = Date()
            now_string.toDate("yyyy-MM-dd HH:mm:ss")?.let {
                now = it
            }

            if (temp_date.before(now)) {
                isTempPlay = false
            }
        }

        //3.如果管理者設定報名臨打名額是0，關閉臨打
        if (myTable!!.people_limit == 0) {
            isTempPlay = false
        }
    }

    fun signupButtonPressed(view: View) {

        if (!member.isLoggedIn) {
            warning("請先登入會員")
            return
        }

        if (!member.checkEMailValidate()) {
            warning("請先通過email認證")
            return
        }

        if (!member.checkMobileValidate()) {
            warning("請先通過行動電話認證")
            return
        }

        if (myTable != null && myTable!!.signupDate != null) {

            var deadline_time: Date? = null
            myTable!!.signupDate!!.deadline.toDate("yyyy-MM-dd HH:mm:ss")?.let {
                deadline_time = it
            }

            if (deadline_time != null) {
                val now: Date = Date()
                if (now.after(deadline_time)) {

                    msg = "已經超過報名截止時間，請下次再報名"
                    if (myTable!!.isSignup) {
                        msg = "已經超過取消報名截止時間，無法取消報名"
                    }
                    warning(msg)
                    return
                }
            } else {
                warning("無法取得報名截止時間，無法報名，請洽管理員")
                return
            }

            Loading.show(mask)
            dataService.signup(this, myTable!!.token, member.token!!, myTable!!.signupDate!!.token) { success->

                runOnUiThread {
                    Loading.hide(mask)
                }

                if (success) {
                    val jsonString: String = dataService.jsonString
                    val successTable: SuccessTable? = jsonToModel(jsonString)
                    if (successTable != null && successTable.success) {
                        runOnUiThread {
                            info(successTable.msg, "", "確定") {
                                refresh()
                            }
                        }
                    } else {
                        if (successTable != null) {
                            runOnUiThread {
                                warning(successTable.msg)
                            }
                        }
                    }
                }
            }
        }
    }

//    override fun didSelectRowAt(view: View, position: Int) {
//
//    }

}