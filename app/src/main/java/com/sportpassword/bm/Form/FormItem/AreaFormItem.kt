package com.sportpassword.bm.Form.FormItem

import com.sportpassword.bm.Controllers.BaseActivity
import com.sportpassword.bm.Form.FormItemCellType
import com.sportpassword.bm.Utilities.AREA_KEY
import com.sportpassword.bm.Utilities.AREA_KEY
import com.sportpassword.bm.Utilities.Global

class AreaFormItem(isRequired: Boolean = false, delegate: BaseActivity? = null) : FormItem(AREA_KEY, "區域", "", null, null, isRequired, delegate) {

    var selected_area_ids: MutableList<Int> = mutableListOf()
    var selected_area_names: MutableList<String> = mutableListOf()
    var areasFromCache: ArrayList<HashMap<String, String>> = arrayListOf()
    var area: HashMap<String, String> = hashMapOf()
    var city_id: Int? = null

    init {
        uiProperties.cellType = FormItemCellType.area
        reset()
    }

    override fun reset() {
        super.reset()
        value = null
        selected_area_ids.clear()
        selected_area_names.clear()
        make()
    }

    override fun make() {

        //value is a number string by divide ,
        valueToAnother()
        show = if (selected_area_names.count() > 0) {
            selected_area_names.joinToString(",")
        } else {
            ""
        }
        if (selected_area_ids.count() > 0) {
            val tmp = selected_area_ids.map {
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
//            getAreas()
            val tmps = value!!.split(",")
            if (city_id != null) {
                val areas = Global.getAreasByCityID(city_id!!)
                for (tmp in tmps) {
                    for (area in areas) {
                        if (area.id == tmp.toInt()) {
                            selected_area_names.add(area.name)
                            break
                        }
                    }
                    val n: Int = tmp.toInt()
                    selected_area_ids.add(n)
                }
            }
        }
    }

//    fun getAreas() {
//        if (this.delegate != null && this.city_id != null) {
//            areasFromCache = delegate!!.getAreasFromCity(this.city_id!!) { rows ->
//                areasFromCache = rows
//                //println(areasFromCache)
//            }
//        }
//
//    }
}