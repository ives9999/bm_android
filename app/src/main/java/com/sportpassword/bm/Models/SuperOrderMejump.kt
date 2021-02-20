package com.sportpassword.bm.Models

import org.json.JSONObject

class SuperOrderMejump(data: JSONObject): SuperModel(data) {

    var id: Int = -1
    var order_id: Int = -1
    var product_id: Int = -1
    var price_id: Int = -1
    var quantity: Int = -1
    var price: Int = -1
    var title: String = ""
    var unit: String = "組"
}