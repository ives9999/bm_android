package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.sportpassword.bm.Data.SelectRow
import com.sportpassword.bm.Models.Area
import com.sportpassword.bm.functions.getAreasByCityID

class SelectAreaVC: SingleSelectVC() {

    var city_id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra("city_id")) {
            city_id = intent.getIntExtra("city_id", 0)
        }
        if (city_id == null || city_id == 0) {
            warning("請先選擇縣市")
        } else {
//            val session_rows = getAreasFromCity(city_id!!) { rows1 ->
//                rows = rows1
//                rowsBridge(rows)
//                notifyChanged()
//            }
            val areas: ArrayList<Area> = getAreasByCityID(city_id!!)
            tableRows = rowsBridge(areas)
            tableAdapter.rows = tableRows
//            notifyChanged()
        }
    }

    fun rowsBridge(areas: ArrayList<Area>): ArrayList<SelectRow> {

        val selectRows: ArrayList<SelectRow> = arrayListOf()

        for(area in areas) {
            val title = area.name
            val id = area.id
            selectRows.add(SelectRow(title, id.toString()))
        }

        return selectRows
    }

//    fun rowsBridge(areas: ArrayList<Area>) {
//
//        if (rows.count() > 0) {
//            rows.clear()
//        } else {
//            rows = arrayListOf()
//        }
//        for(area in areas) {
//            val name = area.name
//            val id = area.id
//            rows!!.add(hashMapOf("title" to name, "value" to id.toString()))
//        }
//    }


//    override fun dealSelected() {
//
//        if (selected != null) {
//            selected = selected!!.toDouble().toInt().toString()
//        }
//    }
}