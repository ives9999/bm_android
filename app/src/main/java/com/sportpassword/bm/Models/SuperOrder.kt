package com.sportpassword.bm.Models

import com.sportpassword.bm.Utilities.ORDER_PROCESS
import com.sportpassword.bm.Utilities.PAYMENT_PROCESS
import com.sportpassword.bm.Utilities.SHIPPING_PROCESS
import com.sportpassword.bm.Utilities.formattedWithSeparator
import com.sportpassword.bm.Utilities.noSec
import org.json.JSONObject

class SuperOrder(data: JSONObject): SuperModel(data) {

    var id: Int = -1
    var order_no: String = ""
    var product_id: Int = 0
    var member_id: Int = 0
    var quantity: Int = 0
    var amount: Int = 0
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

    var product_type: String = ""
    var gateway_ch: String = ""
    var method_ch: String = ""

    var payment: SuperPayment = SuperPayment(data)
    var shipping: SuperShipping = SuperShipping(data)
    var product: SuperProduct = SuperProduct(data)

    var order_clothes: ArrayList<SuperOrderClothes> = arrayListOf()
    var order_racket: ArrayList<SuperOrderRacket> = arrayListOf()
    var order_mejump: ArrayList<SuperOrderMejump> = arrayListOf()

    var attribute: String = ""
    var address: String = ""
    var product_name: String = ""

    var unit: String = "件"
    var quantity_show: String = ""
    var product_price_show: String = ""
    var shipping_fee_show: String = ""
    var tax_show: String = ""
    var amount_show: String = ""

    var created_at_show: String = ""
    var order_process_show: String = ""

    var payment_process_show: String = ""
    var payment_at_show: String = "未付款"

    var shipping_process_show: String = ""
    var shipping_at_show: String = "準備中"

    override fun filter() {
        product_name = product.name
        address = order_city+order_area+order_road
        attribute = makeAttributes()
        quantity_show = "$quantity$unit"

        var product_price: Int = getProductPrice()
        product_price *= quantity

        product_price_show = thousandNumber(product_price)
        shipping_fee_show = thousandNumber(shipping_fee)
        amount_show = thousandNumber(amount)

        created_at_show = created_at.noSec()
        order_process_show = ORDER_PROCESS.getRawValueFromString(process)

        if (payment_at.length > 0) {
            payment_at_show = payment_at.noSec()
        }
        payment_process_show = PAYMENT_PROCESS.getRawValueFromString(payment.process)

        if (shipping_at.length > 0) {
            shipping_at_show = shipping_at.noSec()
        }
        shipping_process_show = SHIPPING_PROCESS.getRawValueFromString(shipping.process)
    }

    private fun makeAttributes(): String {

        var attributes: ArrayList<String> = arrayListOf()
        if (product_type == "clothes") {
            for (item in order_clothes) {
                val color: String = item.color
                val size: String = item.size
                unit = item.unit
                val quantity: String = (item.quantity).toString() + unit
                val price: String = (item.price).toString()
                val attribute =
                "顏色：" + color + "," +
                        "尺寸：" + size + "," +
                        "數量：" + quantity + "," +
                        "價格：" + price
                attributes.add(attribute)
            }
        } else if (product_type == "racket") {
            for (item in order_racket) {
                val color: String = item.color
                val weight: String = item.weight
                unit = item.unit
                val quantity: String = (item.quantity).toString() + unit
                val price: String = (item.price).toString()
                val attribute =
                "顏色：" + color + "," +
                        "重量：" + weight + "," +
                        "數量：" + quantity + "," +
                        "價格：" + price
                attributes.add(attribute)
            }
        } else if (product_type == "mejump") {
            for (item in order_mejump) {
                val title: String = item.title
                unit = item.unit
                val quantity: String = (item.quantity).toString() + unit
                val price: String = (item.price).toString()
                val attribute =
                "種類：" + title + "," +
                        "數量：" + quantity + "," +
                        "價格：" + price
                attributes.add(attribute)
            }
        }
        val attribute: String = attributes.joinToString("\n")

        return attribute
    }

    private fun thousandNumber(m: Int): String {
        val tmp = m.formattedWithSeparator()
        val price: String = "NT$ $tmp"

        return price
    }

    private fun getProductPriceID(): Int {

        var price_id: Int = 0
        if (product_type == "clothes") {
            for (tmp in order_clothes) {
                price_id = tmp.price_id
            }
        } else if (product_type == "racket") {
            for (tmp in order_racket) {
                price_id = tmp.price_id
            }
        } else if (product_type == "mejump") {
            for (tmp in order_mejump) {
                price_id = tmp.price_id
            }
        }
        return price_id
    }

    private fun getProductPrice(): Int {
        val price_id: Int = getProductPriceID()
        val prices: ArrayList<SuperProductPrice> = product.prices
        var product_price: Int = 0
        for (price in prices) {
            if (price.id == price_id) {
                product_price = price.price_member
                break
            }
        }
        return product_price
    }
}