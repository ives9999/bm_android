package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import com.google.gson.JsonParseException
import com.sportpassword.bm.Models.ArenaTable
import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.ArenaService
import com.sportpassword.bm.Utilities.jsonToModel
import kotlinx.android.synthetic.main.activity_show_arena_vc.*
import kotlin.reflect.full.memberProperties

class ShowArenaVC: ShowVC() {

    var myTable: ArenaTable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_show_arena_vc)

        dataService = ArenaService

        refreshLayout = refresh
        setRefreshListener()

        initAdapter()
        super.onCreate(savedInstanceState)

        tableRowKeys = mutableListOf("tel_show","address","fb","interval_show","block","bathroom_show","air_condition_show","parking_show","pv","created_at_show")
        tableRows = hashMapOf(
            "tel_show" to hashMapOf("icon" to "tel","title" to "電話","content" to ""),
            "address" to hashMapOf("icon" to "map","title" to "住址","content" to ""),
            "fb" to hashMapOf("icon" to "fb","title" to "FB","content" to ""),
            "interval_show" to hashMapOf("icon" to "clock","title" to "時段","content" to ""),
            "block" to hashMapOf("icon" to "block","title" to "場地","content" to ""),
            "bathroom_show" to hashMapOf("icon" to "bathroom","title" to "浴室","content" to ""),
            "air_condition_show" to hashMapOf("icon" to "air_condition","title" to "空調","content" to ""),
            "parking_show" to hashMapOf("icon" to "parking","title" to "停車場","content" to ""),
            "pv" to hashMapOf("icon" to "pv","title" to "瀏覽數","content" to ""),
            "created_at_show" to hashMapOf("icon" to "calendar","title" to "建立日期","content" to "")
        )

        refresh()
    }

    override fun genericTable() {
        //println(dataService.jsonString)
        try {
            table = jsonToModel<ArenaTable>(dataService.jsonString)
        } catch (e: JsonParseException) {
            warning(e.localizedMessage!!)
        }
        if (table != null) {
            myTable = table as ArenaTable
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
                    var value = it.getter.call(table).toString()
                    if (value == "null") value = ""
                    if (value == "-1") value = ""
                    tableRows[key]!!["content"] = value
                }
            }
        }

//        val items = generateMainItem()
//        adapter.update(items)

    }

//    override fun didSelectRowAt(view: View, position: Int) {
//
//    }
}