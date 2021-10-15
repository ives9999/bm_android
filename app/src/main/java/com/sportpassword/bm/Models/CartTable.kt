package com.sportpassword.bm.Models

import com.sportpassword.bm.Utilities.formattedWithSeparator

class CartsTable: Tables() {

    var rows: ArrayList<CartTable> = arrayListOf()
}

class CartTable: Table() {

    var order_id: Int = 0
    var member_id: Int = 0

    var cancel_at: String = ""
    var delete_at: String = ""

    var items: ArrayList<CartItemTable> = arrayListOf()
}

class CartItemTable: Table() {

    var cart_id: Int = 0
    var product_id: Int = 0
    var attribute: String = ""
    var amount: Int = 0
    var discount: Int = 0
    var quantity: Int = 0
    var product: ProductTable? = null

    //[name:尺寸]
    //[alias:size]
    //[value:M]
    var attributes: ArrayList<HashMap<String, String>> = arrayListOf()

    var amount_show: String = ""

    override fun filterRow() {
        super.filterRow()

        product?.filterRow()
        attributes.clear()

        //{name:尺寸,alias:size,value:M}|{name:尺寸,alias:size,value:M}
        if (attribute.length > 0) {
            val tmps: Array<String> = attribute.split("|").toTypedArray()
            for (tmp in tmps) {

                //{name:尺寸,alias:size,value:M}
                var _tmp = tmp.replace("{", "")
                _tmp = _tmp.replace("}", "")

                //name:尺寸,alias:size,value:M
                val arr: Array<String> = _tmp.split(",").toTypedArray()

                //[name:尺寸]
                //[alias:size]
                //[value:M]
                val a: HashMap<String, String> = hashMapOf()
                if (arr.isNotEmpty()) {
                    for (str in arr) {
                        val b: Array<String> = str.split(":").toTypedArray()
                        a[b[0]] = b[1]
                    }

                    attributes.add(a)
                }
            }
        }

        if (amount > 0) {
            amount_show = "NT$ ${amount.formattedWithSeparator()} 元"
        } else {
            amount_show = "未提供"
        }
    }
}