package com.sportpassword.bm.Form.FormItem

import android.content.SharedPreferences
import com.sportpassword.bm.App
import com.sportpassword.bm.Controllers.BaseActivity
import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.Models.City
import com.sportpassword.bm.Utilities.CITY_KEY
import com.sportpassword.bm.Utilities.SESSION_FILENAME
import com.sportpassword.bm.Utilities.TT_COLOR
import com.sportpassword.bm.Utilities.getAllCitys

class CityFormItem: FormItem {

    var selected_city_ids: MutableList<Int> = mutableListOf()
    var selected_city_names: MutableList<String> = mutableListOf()

    var citysFromCache: ArrayList<HashMap<String, String>> = arrayListOf()

    init {
        uiProperties.cellType = FormItemCellType.city
    }

    constructor(isRequired: Boolean = false, delegate: BaseActivity? = null): super(CITY_KEY, "縣市", "", null, null, isRequired, delegate) {

        if (this.delegate != null) {
            citysFromCache = delegate!!.getCitys { rows ->
                citysFromCache = rows
            }
        }

        reset()
    }

    override fun reset() {
        super.reset()
        value = null
        selected_city_ids.clear()
        selected_city_names.clear()
        make()
    }

    override fun make() {

        //value is a number string by divide ,
        valueToAnother()
        show = if (selected_city_names.count() > 0) {
            selected_city_names.joinToString(",")
        } else {
            ""
        }
        if (selected_city_ids.count() > 0) {
            val tmp = selected_city_ids.map {
                it.toString()
            }
            sender = tmp.joinToString(",")
            value = sender as String
        } else {
            sender = null
        }
    }

    //parse value to array
    override fun valueToAnother() {
        //value is 1,2,3 or 1 is city id
        if (value != null && value!!.count() > 0) {
            val tmps = value!!.split(",")
            for (tmp in tmps) {
                for (city in citysFromCache) {
                    if (city["id"] == tmp) {
                        selected_city_names.add(city["name"]!!)
                        break
                    }
                }
                val n: Int = tmp.toInt()
                selected_city_ids.add(n)
            }
        }
    }
}