package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import com.google.gson.JsonParseException
import com.sportpassword.bm.Data.MemberRow
import com.sportpassword.bm.Data.MemberSection
import com.sportpassword.bm.Data.ShowRow
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.Models.TeamTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeamService
import com.sportpassword.bm.Utilities.jsonToModel
import kotlinx.android.synthetic.main.activity_show_team_vc.refresh
import kotlin.reflect.full.memberProperties

class ShowTeamVC: ShowVC() {

    var myTable: TeamTable? = null

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

        //init()
        refresh()
    }

//    override fun init() {
//        super.init()
//
//        showRows.addAll(arrayListOf(
//            ShowRow("arena", "arena1", "球館"),
//            ShowRow("interval_show", "clock", "時段"),
//            ShowRow("ball", "ball", "球種"),
//            ShowRow("leader", "group", "管理者"),
//            ShowRow("mobile_show", "mobile", "行動電話"),
//            ShowRow("line", "line", "line"),
//            ShowRow("fb", "fb", "FB"),
//            ShowRow("youtube", "youtube", "Youtube"),
//            ShowRow("website", "website", "網站"),
//            ShowRow("email", "email1", "EMail"),
//            ShowRow("pv", "pv", "瀏覽數"),
//            ShowRow("created_at_show", "date", "建立日期")
//        ))
//    }

    override fun initData() {

        if (myTable == null) {
            myTable = TeamTable()
        }

        myTable = table as? TeamTable
        var row: MemberRow = MemberRow("球館", "arena1", myTable!!.arena!!.name)
        memberRows.add(row)
        row = MemberRow("時段", "clock", myTable!!.interval_show)
        memberRows.add(row)
        row = MemberRow("球種", "ball", myTable!!.ball)
        memberRows.add(row)
        row = MemberRow("程度", "degree", myTable!!.degree_show)
        memberRows.add(row)
        row = MemberRow("隊長", "group", myTable!!.manager_nickname)
        memberRows.add(row)
        row = MemberRow("行動電話", "mobile", myTable!!.mobile_show)
        memberRows.add(row)
        row = MemberRow("line", "line", myTable!!.line)
        memberRows.add(row)
        row = MemberRow("FB", "fb", myTable!!.fb)
        memberRows.add(row)
        row = MemberRow("Youtube", "youtube", myTable!!.youtube)
        memberRows.add(row)
        row = MemberRow("網站", "website", myTable!!.website)
        memberRows.add(row)
        row = MemberRow("EMail", "email1", myTable!!.email)
        memberRows.add(row)
        row = MemberRow("瀏覽數", "pv", myTable!!.pv.toString())
        memberRows.add(row)
        row = MemberRow("建立日期", "date", myTable!!.created_at_show)
        memberRows.add(row)

        val memberSection: MemberSection = MemberSection("", true, memberRows)
        memberSections.add(memberSection)
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

    override fun tableToPage() {
        //table!!.filterRow()

        isLike = table!!.like
        likeCount = table!!.like_count

        runOnUiThread {
            if (table!!.name.isNotEmpty()) {
                setMyTitle(table!!.name)
            } else if (table!!.title.isNotEmpty()) {
                setMyTitle(table!!.title)
            }
            setFeatured()
            setData()
            setContent()
            memberSectionAdapter.notifyDataSetChanged()
//            showAdapter.rows = showRows
//            showAdapter.notifyDataSetChanged()
            setLike()
        }
    }

    override fun setData() {

//        if (myTable != null) {
//            setMainData(myTable!!)
//        }
    }

//    override fun setMainData(table: Table) {
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
//
////        val items = generateMainItem()
////        adapter.update(items)
//
//    }

//    override fun didSelectRowAt(view: View, position: Int) {
//
//    }

}