package com.sportpassword.bm.Models

import org.json.JSONObject

class SuperPayment(data: JSONObject): SuperModel(data) {

    @objc dynamic var id: Int = -1
    @objc dynamic var order_id: Int = -1
    @objc dynamic var gateway: String = ""
    @objc dynamic var amount: Int = 0
    @objc dynamic var shipping_fee: Int = 0
    @objc dynamic var handling_fee: Int = 0
    @objc dynamic var tax: Int = 0

    @objc dynamic var discount: Int = 0
    @objc dynamic var process: String = ""
    @objc dynamic var trade_no: String = ""
    @objc dynamic var barcode1: String = ""
    @objc dynamic var barcode2: String = ""
    @objc dynamic var barcode3: String = ""

    @objc dynamic var expire_at: String = ""
    @objc dynamic var created_at: String = ""
    @objc dynamic var updated_at: String = ""
}