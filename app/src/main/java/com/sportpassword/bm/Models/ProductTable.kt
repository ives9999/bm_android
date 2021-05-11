package com.sportpassword.bm.Models

import com.sportpassword.bm.Utilities.BASE_URL

class ProductsTable: Tables() {
    var rows: ArrayList<ProductTable> = arrayListOf()
}

class ProductTable: Table() {

    var type: String = ""
    var color: String = ""
    var size: String = ""
    var weight: String = ""
    var shipping: String = ""
    var gateway: String = ""
    var order_min: Int = 1
    var order_max: Int = 1

    var alias: String = ""

    var images: ArrayList<String> = arrayListOf()
    var prices: ArrayList<ProductPriceTable> = arrayListOf()
    var colors: ArrayList<String> = arrayListOf()
    var sizes: ArrayList<String> = arrayListOf()
    var weights: ArrayList<String> = arrayListOf()
    var shippings: ArrayList<String> = arrayListOf()
    var gateways: ArrayList<String> = arrayListOf()

    override fun filterRow() {

        super.filterRow()
        if (images.size > 0) {
            for ((idx, image) in images.withIndex()) {
                if (!image.startsWith("http://") || !image.startsWith("https://")) {
                    val _image = BASE_URL + image
                    images[idx] = _image
                }
            }
        }
    }
}

class ProductPriceTable: Table() {

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

    override fun filterRow() {

        super.filterRow()
    }
}