package com.sportpassword.bm.Controllers

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import com.sportpassword.bm.App
import com.sportpassword.bm.R
import com.sportpassword.bm.Utilities.CITY_KEY
import com.sportpassword.bm.Utilities.Loading
import com.sportpassword.bm.Utilities.SESSION_FILENAME
import kotlinx.android.synthetic.main.mask.*
import org.json.JSONArray
import org.json.JSONObject

open class SelectVC1 : MyTableVC1() {

    var title: String = "選擇"
    var key: String? = null

    val session: SharedPreferences = App.instance.getSharedPreferences(SESSION_FILENAME, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (intent.hasExtra("title")) {
            title = intent.getStringExtra("title")
        }
        setMyTitle(title)

        if (intent.hasExtra("key")) {
            key = intent.getStringExtra("key")
        }
        if (key == null) {
            warningWithPrev("由於傳遞參數不正確，無法做選擇，請回上一頁重新進入")
        }

        if (intent.hasExtra("rows")) {
            rows = intent.getSerializableExtra("rows") as ArrayList<HashMap<String, String>>
        }
    }

    fun init() {
        if (key != null && key == CITY_KEY) {
            getCitys()
        }
    }

    fun getCitys() {
        Loading.show(mask)
        dataService.getCitys(this, "all", false) { success->

            if (success) {
                val citys = dataService.citys
                val arr: JSONArray = JSONArray()
                rows = arrayListOf()
                for (city in citys) {
                    val title = city.name
                    val value = city.id.toString()
                    rows.add(hashMapOf("title" to title, "value" to value))
                    val obj = JSONObject()
                    obj.put("title", title)
                    obj.put("value", value)
                    arr.put(obj)
                }

                if (arr.length() > 0) {
                    session.edit().putString("citys", arr.toString()).apply()
                }
            }
            notifyChanged()
            Loading.hide(mask)
        }
    }

    fun cancel(view: View) {
        prev()
    }
}
