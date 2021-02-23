package com.sportpassword.bm.Models

import org.json.JSONObject

class SuperOrderRacket(data: JSONObject): SuperModel(data) {

    var id: Int = -1
    var order_id: Int = -1
    var product_id: Int = -1
    var price_id: Int = -1
    var color: String = ""
    var weight: String = ""
    var quantity: Int = -1
    var price: Int = -1
    var unit: String = "隻"
}