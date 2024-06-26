package com.sportpassword.bm.Controllers

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.gson.JsonParseException
import com.sportpassword.bm.Data.ShowRow
import com.sportpassword.bm.Models.ArenaTable
import com.sportpassword.bm.R
import com.sportpassword.bm.Services.ArenaService
import com.sportpassword.bm.Views.ShowTop2
import com.sportpassword.bm.databinding.ActivityShowArenaVcBinding
import com.sportpassword.bm.functions.jsonToModel

class ShowArenaVC: ShowVC() {

    private lateinit var binding: ActivityShowArenaVcBinding
    private lateinit var view: ViewGroup

    var showTop2: ShowTop2? = null

    var myTable: ArenaTable? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityShowArenaVcBinding.inflate(layoutInflater)
        view = binding.root
        setContentView(view)

        dataService = ArenaService

        refreshLayout = binding.refresh
        setRefreshListener()

        //initAdapter()
        super.onCreate(savedInstanceState)

//        tableRowKeys = mutableListOf("tel_show","address","fb","interval_show","block","bathroom_show","air_condition_show","parking_show","pv","created_at_show")
//        tableRows = hashMapOf(
//            "tel_show" to hashMapOf("icon" to "tel","title" to "電話","content" to ""),
//            "address" to hashMapOf("icon" to "map","title" to "住址","content" to ""),
//            "fb" to hashMapOf("icon" to "fb","title" to "FB","content" to ""),
//            "interval_show" to hashMapOf("icon" to "clock","title" to "時段","content" to ""),
//            "block" to hashMapOf("icon" to "block","title" to "場地","content" to ""),
//            "bathroom_show" to hashMapOf("icon" to "bathroom","title" to "浴室","content" to ""),
//            "air_condition_show" to hashMapOf("icon" to "air_condition","title" to "空調","content" to ""),
//            "parking_show" to hashMapOf("icon" to "parking","title" to "停車場","content" to ""),
//            "pv" to hashMapOf("icon" to "pv","title" to "瀏覽數","content" to ""),
//            "created_at_show" to hashMapOf("icon" to "calendar","title" to "建立日期","content" to "")
//        )

        findViewById<Button>(R.id.signupButton) ?. let {
            it.visibility = View.GONE
        }

        findViewById<ShowTop2>(R.id.top) ?. let {
            showTop2 = it
            it.showPrev(true)
        }

        init()
        refresh()
    }

    override fun init() {
        super.init()
    }

    override fun initData() {
        super.initData()

        showRows.addAll(arrayListOf(
            ShowRow("tel_show", "tel", "電話"),
            ShowRow("address", "map", "住址"),
            ShowRow("fb", "fb", "FB"),
            ShowRow("interval_show", "clock", "時段"),
            ShowRow("block_show", "block", "場地"),
            ShowRow("bathroom_show", "bathroom", "浴室"),
            ShowRow("air_condition_show", "air_condition", "空調"),
            ShowRow("parking_show", "parking", "停車場"),
            ShowRow("pv", "pv", "瀏覽數"),
            ShowRow("created_at_show", "date", "建立日期")
        ))
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
            showTop2!!.setTitle(myTable!!.name)
        } else {
            warning("解析伺服器所傳的字串失敗，請洽管理員")
        }
    }

    override fun setData() {

        if (myTable != null) {
            setMainData(myTable!!)
        }
    }


//    override fun didSelectRowAt(view: View, position: Int) {
//
//    }
}