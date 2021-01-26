package com.sportpassword.bm.Models

import org.json.JSONObject

class SuperOrder(data: JSONObject): SuperModel(data) {

    var id: Int = -1
    var order_no: String = ""
    var product_id: Int = 0
    var member_id: Int = 0
    var quantity: Int = 0
    var shipping_fee: Int = 0
    var tax: Int = 0

    var order_name: String = ""
    var order_tel: String = ""
    var order_email: String = ""
    var order_city: String = ""
    var order_area: String = ""
    var order_road: String = ""

    var memo: String = ""
    var process: String = ""

    var status: String = "online"
    var token: String = ""
    var channel: String = ""

    var shipping_at: String = ""
    var payment_at: String = ""
    var complete_at: String = ""
    var cancel_at: String = ""

    var created_at: String = ""
    var updated_at: String = ""

    var payment: SuperPayment = SuperPayment(data)
    var shipping: SuperShipping = SuperShipping(data)
    var product: SuperProduct = SuperProduct(data)
}