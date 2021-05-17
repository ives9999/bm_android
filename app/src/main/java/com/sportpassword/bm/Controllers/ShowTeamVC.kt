package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import com.google.gson.JsonParseException
import com.sportpassword.bm.Adapters.IconCellDelegate
import com.sportpassword.bm.Models.CourseTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.Models.TeamTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.jsonToModel
import com.xwray.groupie.GroupAdapter
import kotlinx.android.synthetic.main.activity_show_course_vc.*
import kotlinx.android.synthetic.main.activity_show_team_vc.*
import kotlinx.android.synthetic.main.activity_show_team_vc.refresh
import kotlin.reflect.full.memberProperties

class ShowTeamVC: ShowVC(), IconCellDelegate {

    var myTable: TeamTable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_show_team_vc)

        dataService = TeamService

        refreshLayout = refresh
        setRefreshListener()

        initAdapter()
        super.onCreate(savedInstanceState)

        tableRowKeys = mutableListOf("arena","interval_show","ball","leader","mobile_show","fb","youtube","website","email","pv","created_at_show")
        tableRows = hashMapOf(
            "arena" to hashMapOf("icon" to "arena","title" to "球館","content" to ""),
            "interval_show" to hashMapOf("icon" to "clock","title" to "時段","content" to ""),
            "ball" to hashMapOf("icon" to "ball","title" to "球種","content" to ""),
            "leader" to hashMapOf("icon" to "member1","title" to "隊長","content" to ""),
            "mobile_show" to hashMapOf("icon" to "mobile","title" to "行動電話","content" to ""),
            "fb" to  hashMapOf("icon" to "fb","title" to "FB","content" to ""),
            "youtube" to hashMapOf("icon" to "youtube","title" to "Youtube","content" to ""),
            "website" to hashMapOf("icon" to "website","title" to "網站","content" to ""),
            "email" to hashMapOf("icon" to "email1","title" to "EMail","content" to ""),
            "pv" to hashMapOf("icon" to "pv","title" to "瀏覽數","content" to ""),
            "created_at_show" to hashMapOf("icon" to "calendar","title" to "建立日期","content" to "")
        )

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
        }
    }

    override fun setMainData(table: Table) {
        for (key in tableRowKeys) {
            val kc = table::class
            kc.memberProperties.forEach {
                if (key == it.name) {
                    val value = it.getter.call(table).toString()
                    tableRows[key]!!["content"] = value

                    if (key == "arena") {
                        if (myTable!!.arena != null) {
                            tableRows["arena"]!!["content"] = myTable!!.arena!!.name
                        } else {
                            tableRows["arena"]!!["content"] = "未提供"
                        }
                    }
                }
            }
        }

        val items = generateMainItem()
        adapter.update(items)

    }

    override fun didSelectRowAt(view: View, position: Int) {

    }

}