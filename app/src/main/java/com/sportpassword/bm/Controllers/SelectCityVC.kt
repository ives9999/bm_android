package com.sportpassword.bm.Controllers

import android.os.Bundle
import com.sportpassword.bm.Models.City
import com.sportpassword.bm.Services.DataService
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.getAllCitys
import kotlinx.android.synthetic.main.mask.*
import org.json.JSONArray
import org.json.JSONObject

class SelectCityVC: SingleSelectVC1() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val session_rows = getCitys { rows1 ->
            rows = rows1
            rowsBridge(rows)
            notifyChanged()
        }
        rowsBridge(session_rows)
        notifyChanged()


//        val citys: ArrayList<City> = session.getAllCitys()
//        //println(citys)
//        if (citys.count() > 0) {
//
//            rows = arrayListOf()
//            for (city in citys) {
//                val name = city.name
//                val id = city.id.toString()
//                rows.add(hashMapOf("title" to name, "value" to id))
//            }
//            notifyChanged()
//        }  else {
//            Loading.show(mask)
//            dataService.getCitys(this, "all", false) { success ->
//                if (success) {
//                    val citys = dataService.citys
//                    val arr: JSONArray = JSONArray()
//                    rows = arrayListOf()
//                    for (city in citys) {
//                        val name = city.name
//                        val id = city.id.toString()
//                        rows.add(hashMapOf("title" to name, "value" to id))
//                        val obj = JSONObject()
//                        obj.put("name", name)
//                        obj.put("id", id)
//                        arr.put(obj)
//                    }
//
//                    if (arr.length() > 0) {
//                        session.edit().putString("citys", arr.toString()).apply()
//                    }
//                    notifyChanged()
//                }
//            }
//            Loading.hide(mask)
//        }
    }

}