package com.sportpassword.bm.Services

import com.sportpassword.bm.Models.*
import com.sportpassword.bm.Utilities.JSONParse
import com.sportpassword.bm.Utilities.URL_ONE
import com.sportpassword.bm.Utilities.URL_PRODUCT_LIST
import org.json.JSONObject

object ProductService: DataService() {

    lateinit var superProducts: SuperProducts
    lateinit var superProduct: SuperProduct

    override fun getListURL(): String {
        return URL_PRODUCT_LIST
    }

    override fun getOneURL(): String {
        return URL_ONE.format("product")
    }

    override fun parseModels(json: JSONObject): SuperModel {
        return JSONParse.parse<SuperProducts>(json)!!
    }

    override fun parseModel(json: JSONObject): SuperModel {
        return JSONParse.parse<SuperProduct>(json)!!
    }
}