package com.sportpassword.bm.Models

import org.json.JSONObject

class SuperProduct(data: JSONObject): SuperModel(data) {

    var id: Int = -1
    var name: String = ""
    var channel: String = ""
    var slug: String = ""

    var type: String = ""
    var color: String = ""
    var size: String = ""
    var weight: String = ""
    var shipping: String = ""
    var gateway: String = ""
    var order_min: Int = 1
    var order_max: Int = 1

    var content: String = ""
    var alias: String = ""

    var status: String = "online"
    var token: String = ""
    var sort_order: Int = 0
    var pv: Int = 0
    var created_id: Int = 0
    var created_at: String = ""
    var updated_at: String = ""
    var created_at_text: String = ""
    var featured_path: String = ""
    var thumb: String = ""

    var images: ArrayList<String> = arrayListOf()

    var prices: ArrayList<SuperProductPrice> = arrayListOf()
    var colors: ArrayList<String> = arrayListOf()
    var sizes: ArrayList<String> = arrayListOf()
    var weights: ArrayList<String> = arrayListOf()
    var shippings: ArrayList<String> = arrayListOf()
    var gateways: ArrayList<String> = arrayListOf()
}

class SuperProducts(data: JSONObject): SuperModel(data) {
    var success: Boolean = true
    var page: Int = 0
    var totalCount: Int = 0
    var perPage: Int = 0
    var rows: ArrayList<SuperProduct> = arrayListOf()
}