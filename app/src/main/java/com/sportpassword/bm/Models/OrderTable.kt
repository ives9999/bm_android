package com.sportpassword.bm.Models

import com.sportpassword.bm.Utilities.*

class OrdersTable: Tables() {
    var rows: ArrayList<OrderTable> = arrayListOf()
}

class OrderTable: Table() {

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

    var shipping_at: String = ""
    var payment_at: String = ""
    var complete_at: String = ""
    var cancel_at: String = ""

    var product_type: String = ""
    var gateway_ch: String = ""
    var method_ch: String = ""

    var payment: PaymentTable? = null
    var shipping: ShippingTable? = null
    var product: ProductTable? = null

    var order_clothes: ArrayList<OrderClothesTable> = arrayListOf()
    var order_racket: ArrayList<OrderRacketTable> = arrayListOf()
    var order_mejump: ArrayList<OrderMeJumpTable> = arrayListOf()

    var attribute: String = ""
    var unit: String = "件"

    var product_name: String = ""
    var quantity_show: String = ""
    var product_price_show: String = ""
    var shipping_fee_show: String = ""
    var tax_show: String = ""
    var amount_show: String = ""

    var order_process_show: String = ""

    var payment_process_show: String = ""
    var payment_at_show: String = "未付款"

    var shipping_process_show: String = ""
    var shipping_at_show: String = "準備中"

    override fun filterRow() {

        super.filterRow()

        if (product != null) {
            product_name = product!!.name
        }
        address = order_city+order_area+order_road
        attribute = makeAttributes()
        quantity_show = "${quantity}${unit}"

        var product_price: Int = getProductPrice()
        product_price = product_price * quantity

        product_price_show = thousandNumber(product_price) + "元"
        shipping_fee_show = thousandNumber(shipping_fee) + "元"
        amount_show = thousandNumber(amount) + "元"

        created_at_show = created_at.noSec()
        
        if (process.length > 0) {
            order_process_show = ORDER_PROCESS.getRawValueFromString(process)
        }

        if (payment_at.length > 0) {
            payment_at_show = payment_at.noSec()
        }
        if (payment != null) {
            payment_process_show = PAYMENT_PROCESS.getRawValueFromString(payment!!.process)
        } else {
            payment_process_show = "沒有取得 process table 錯誤"
        }

        if (shipping_at.length > 0) {
            shipping_at_show = shipping_at.noSec()
        }
        if (shipping != null) {
            shipping_process_show = SHIPPING_PROCESS.getRawValueFromString(shipping!!.process)
        } else {
            payment_process_show = "沒有取得 shipping table 錯誤"
        }
    }

    private fun makeAttributes(): String {

        var attributes: ArrayList<String> = arrayListOf()
        if (product_type == "clothes") {
            for (item in order_clothes) {
                val color: String = item.color
                val size: String = item.size
                unit = item.unit
                val quantity: String = item.quantity.toString() + unit
                val price: String = item.price.toString()
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
                val quantity: String = item.quantity.toString() + unit
                val price: String = item.price.toString()
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
                val quantity: String = item.quantity.toString() + unit
                val price: String = item.price.toString()
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
        val price: String = "NT$ ${tmp}"

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
        //val price_id: Int = getProductPriceID()

        var product_price: Int = 9999999
        if (product != null && product!!.prices.size > 0) {
            val prices: ArrayList<ProductPriceTable> = product!!.prices
            product_price = prices[0].price_member
//            for price in prices {
//                if price.id == price_id {
//                    product_price = price.price_member
//                    break
//                }
//            }
        }
        return product_price
    }
}

class OrderClothesTable: Table() {

    var order_id: Int = -1
    var product_id: Int = -1
    var price_id: Int = -1
    var color: String = ""
    var size: String = ""
    var quantity: Int = -1
    var price: Int = -1
    var unit: String = "件"
}

class OrderRacketTable: Table() {

    var order_id: Int = -1
    var product_id: Int = -1
    var price_id: Int = -1
    var color: String = ""
    var weight: String = ""
    var quantity: Int = -1
    var price: Int = -1
    var unit: String = "隻"
}

class OrderMeJumpTable: Table() {

    var order_id: Int = -1
    var product_id: Int = -1
    var price_id: Int = -1
    var quantity: Int = -1
    var price: Int = -1
    var unit: String = "組"
}