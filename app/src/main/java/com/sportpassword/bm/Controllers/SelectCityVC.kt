package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.sportpassword.bm.Models.City
import com.sportpassword.bm.Utilities.getAllCitys
import org.json.JSONObject

class SelectCityVC: SingleSelectVC1() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val citys: ArrayList<City> = session.getAllCitys()
        //println(citys)

        rows = arrayListOf()
        for (city in citys) {
            val name = city.name
            val id = city.id.toString()
            rows.add(hashMapOf("title" to name, "value" to id))
        }
        notifyChanged()
    }

}