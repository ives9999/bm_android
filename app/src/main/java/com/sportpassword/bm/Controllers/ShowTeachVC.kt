package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.google.gson.JsonParseException
import com.sportpassword.bm.Models.StoreTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.Models.TeachTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeachService
import com.sportpassword.bm.Utilities.jsonToModel
import kotlinx.android.synthetic.main.activity_show_course_vc.*
import kotlin.reflect.full.memberProperties

class ShowTeachVC : ShowVC() {

    var myTable: TeachTable? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        dataService = TeachService

        refreshLayout = refresh
        setRefreshListener()

        initAdapter()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_teach_vc)

        tableRowKeys = mutableListOf("tel_show","mobile_show","address","fb","line","website","email","interval_show","pv","created_at_show")
        tableRows = hashMapOf(
            "tel_show" to hashMapOf("icon" to "tel","title" to "市內電話","content" to ""),
            "mobile_show" to hashMapOf( "icon" to "mobile","title" to "行動電話","content" to ""),
            "address" to hashMapOf( "icon" to "marker","title" to "住址","content" to ""),
            "fb" to hashMapOf( "icon" to "fb","title" to "FB","content" to ""),
            "line" to hashMapOf( "icon" to "lineicon","title" to "line","content" to ""),
            "website" to hashMapOf( "icon" to "website","title" to "網站","content" to ""),
            "email" to hashMapOf( "icon" to "email1","title" to "email","content" to ""),
            "interval_show" to hashMapOf( "icon" to "clock","title" to "營業時間","content" to ""),
            "pv" to hashMapOf( "icon" to "pv","title" to "瀏覽數","content" to ""),
            "created_at_show" to hashMapOf( "icon" to "calendar","title" to "建立日期","content" to "")
        )

        refresh()
    }

    override fun genericTable() {
        //println(dataService.jsonString)
        try {
            table = jsonToModel<TeachTable>(dataService.jsonString)
        } catch (e: JsonParseException) {
            warning(e.localizedMessage!!)
            //println(e.localizedMessage)
        }
        if (table != null) {
            myTable = table as TeachTable
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
                }
            }
        }

        val items = generateMainItem()
        adapter.update(items)
    }
}