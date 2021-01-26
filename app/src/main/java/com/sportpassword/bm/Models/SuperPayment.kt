package com.sportpassword.bm.Models

import org.json.JSONObject

class SuperPayment(data: JSONObject): SuperModel(data) {

    var id: Int = -1
    var order_id: Int = -1
    var gateway: String = ""
    var amount: Int = 0
    var shipping_fee: Int = 0
    var handling_fee: Int = 0
    var tax: Int = 0

    var discount: Int = 0
    var process: String = ""
    var trade_no: String = ""
    var barcode1: String = ""
    var barcode2: String = ""
    var barcode3: String = ""

    var expire_at: String = ""
    var created_at: String = ""
    var updated_at: String = ""
}