package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.sportpassword.bm.Data.SelectRow
import com.sportpassword.bm.Models.City
import com.sportpassword.bm.Utilities.CITY_KEY
import com.sportpassword.bm.Utilities.Global

class SelectCityVC: SingleSelectVC() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        key = CITY_KEY

        val citys: ArrayList<City> = Global.getCitys()
        tableRows = rowsBridge(citys)
        tableAdapter.rows = tableRows
        //notifyChanged()
    }

    fun rowsBridge(citys: ArrayList<City>): ArrayList<SelectRow> {

        val selectRows: ArrayList<SelectRow> = arrayListOf()

        for(city in citys) {
            val title = city.name
            val id = city.id
            selectRows.add(SelectRow(title, id.toString()))
        }

        return selectRows
    }

//    fun rowsBridge(citys: ArrayList<City>) {
//
//        val selectRows: ArrayList<SelectRow> = arrayListOf()
//
//        if (rows.count() > 0) {
//            rows.clear()
//        } else {
//            rows = arrayListOf()
//        }
//        for(city in citys) {
//            val name = city.name
//            val id = city.id
//            rows.add(hashMapOf("title" to name, "value" to id.toString()))
//        }
//    }
}