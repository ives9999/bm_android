package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.google.gson.JsonParseException
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.Models.TeachTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.TeachService
import com.sportpassword.bm.Utilities.jsonToModel
import com.xwray.groupie.GroupAdapter
import kotlinx.android.synthetic.main.activity_show_course_vc.*
import kotlin.reflect.full.memberProperties
import kotlinx.android.synthetic.main.activity_show_teach_vc.*
import kotlinx.android.synthetic.main.activity_show_teach_vc.refresh
import kotlinx.android.synthetic.main.activity_show_teach_vc.tableView

class ShowTeachVC : ShowVC() {

    var myTable: TeachTable? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        setContentView(R.layout.activity_show_teach_vc)
        dataService = TeachService

        refreshLayout = refresh
        setRefreshListener()

        initAdapter()

        super.onCreate(savedInstanceState)

        tableRowKeys = mutableListOf("pv","created_at_show")
        tableRows = hashMapOf(
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