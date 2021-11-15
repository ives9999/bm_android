package com.sportpassword.bm.Controllers

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.App
import com.sportpassword.bm.Data.SelectRow
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.CITY_KEY
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.SESSION_FILENAME
import kotlinx.android.synthetic.main.mask.*
import org.json.JSONArray
import org.json.JSONObject

open class SelectVC : MyTableVC() {

    var title: String = "選擇"
    var key: String? = null
    var tableRows: ArrayList<SelectRow> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra("title")) {
            title = intent.getStringExtra("title")!!
        }
        setMyTitle(title)

        if (intent.hasExtra("key")) {
            key = intent.getStringExtra("key")!!
        }

        if (intent.hasExtra("rows")) {
            //rows = intent.getSerializableExtra("rows") as ArrayList<HashMap<String, String>>
        }
    }

    override fun init() {
        isPrevIconShow = true
        super.init()
    }

//    fun init() {
//        if (key != null && key == CITY_KEY) {
//            getCitys()
//        }
//    }

    //已經不再使用了，直接使用session的getAllCitys
//    fun getCitys() {
//        Loading.show(mask)
//        dataService.getCitys(this, "all", false) { success->
//
//            if (success) {
//                val citys = dataService.citys
//                val arr: JSONArray = JSONArray()
//                rows = arrayListOf()
//                for (city in citys) {
//                    val name = city.name
//                    val id = city.id.toString()
//                    rows.add(hashMapOf("name" to name, "id" to id))
//                    val obj = JSONObject()
//                    obj.put("name", name)
//                    obj.put("id", id)
//                    arr.put(obj)
//                }
//
//                if (arr.length() > 0) {
//                    session.edit().putString("citys", arr.toString()).apply()
//                }
//            }
//            notifyChanged()
//            Loading.hide(mask)
//        }
//    }

    fun cancel(view: View) {
        prev()
    }
}
