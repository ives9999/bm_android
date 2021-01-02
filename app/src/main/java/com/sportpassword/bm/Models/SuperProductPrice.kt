package com.sportpassword.bm.Models

import org.json.JSONObject

class SuperProductPrice(data: JSONObject): SuperModel(data) {

    var id: Int = -1
    var product_id: Int = -1
    var price_title: String = ""
    var price_title_alias: String = ""
    var price_member: Int = -1
    var price_nonmember: Int = -1
    var price_dummy: Int = -1
    var price_desc: String = ""
    var shipping_fee: Int = -1
    var shipping_fee_unit: Int = -1
    var shippint_fee_desc: String = ""
    var tax: Int = -1
}