package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.sportpassword.bm.Models.City
import com.sportpassword.bm.Utilities.CITY_KEY
import com.sportpassword.bm.Utilities.Global

class SelectCityVC: SingleSelectVC() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        key = CITY_KEY

        val citys: ArrayList<City> = Global.getCitys()
        rowsBridge(citys)
        //notifyChanged()
    }

    fun rowsBridge(citys: ArrayList<City>) {

        if (rows.count() > 0) {
            rows.clear()
        } else {
            rows = arrayListOf()
        }
        for(city in citys) {
            val name = city.name
            val id = city.id
            rows.add(hashMapOf("title" to name, "value" to id.toString()))
        }
    }
}