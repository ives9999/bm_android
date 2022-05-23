package com.sportpassword.bm.Models

import com.sportpassword.bm.Utilities.*
import java.time.LocalDateTime
import java.util.*

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
    var total: Int = 0
    var promo: String = ""
    var discount: Int = 0
    var grand_total: Int = 0
    var handle_fee: Int = 0

    var invoice_type: String = ""
    var invoice_no: String = ""
    var invoice_at: String = ""
    var invoice_email: String = ""
    var invoice_company_name: String = ""
    var invoice_company_tax: String = ""

    var order_name: String = ""
    var order_tel: String = ""
    var order_email: String = ""
    var order_city: String = ""
    var order_area: String = ""
    var order_road: String = ""
    var order_address: String = ""

    var memo: String = ""
    var process: String = ""
    var all_process: Int = 0

    var ecpay_token: String = ""
    var ecpay_token_ExpireDate: String = ""

    //var shipping_at: String = ""
    //var payment_at: String = ""
    var complete_at: String = ""
    var cancel_at: String = ""

    var product_type: String = ""
    //var gateway_ch: String = ""
    //var method_ch: String = ""

    var payment: PaymentTable? = null
    var gateway: GatewayTable? = null
    var shipping: ShippingTable? = null
    var product: ProductTable? = null
    var `return`: ReturnTable? = null

    var items: ArrayList<OrderItemTable> = arrayListOf()

    var attribute: String = ""
    var unit: String = "件"

    var product_name: String = ""
    var quantity_show: String = ""
    var product_price_show: String = ""
    var shipping_fee_show: String = ""
    var tax_show: String = ""
    var amount_show: String = ""
    var total_show: String = ""
    var discount_show: String = ""
    var grand_total_show: String = ""
    var order_tel_show: String = ""

    var order_process_show: String = ""
    var all_process_show: String = ""

//    var payment_process_show: String = ""
//    var payment_at_show: String = "未付款"

//    var shipping_process_show: String = ""
//    var shipping_at_show: String = "準備中"

    var invoice_type_show: String = ""
    var canReturn: Boolean = false

    override fun filterRow() {

        super.filterRow()
        gateway?.filterRow()
        shipping?.filterRow()
        `return`?.filterRow()

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
        tax_show = thousandNumber(tax) + "元"
        total_show = thousandNumber(total) + "元"
        discount_show = thousandNumber(discount) + "元"
        grand_total_show = thousandNumber(grand_total) + "元"

        created_at_show = created_at.noSec()
        
        if (process.length > 0) {

            order_process_show = ORDER_PROCESS.getRawValueFromString(process)
            all_process_show = ALL_PROCESS.intToEnum(all_process).chineseName
        }

//        if (payment_at.length > 0) {
//            payment_at_show = payment_at.noSec()
//        }
//        if (payment != null) {
//            payment_process_show = PAYMENT_PROCESS.getRawValueFromString(payment!!.process)
//        } else {
//            payment_process_show = "沒有取得 process table 錯誤"
//        }
//
//        if (shipping_at.length > 0) {
//            shipping_at_show = shipping_at.noSec()
//        }
//        if (shipping != null) {
//            shipping_process_show = SHIPPING_PROCESS.getRawValueFromString(shipping!!.process)
//        } else {
//            payment_process_show = "沒有取得 shipping table 錯誤"
//        }

        if (invoice_type == "company") {
            invoice_type_show = "公司行號"
        } else {
            invoice_type_show = "個人"
        }

        if (invoice_no == null) {
            invoice_no = ""
        }

        if (invoice_at == null) {
            invoice_at = ""
        }

        order_tel_show = order_tel.mobileShow()

        if (all_process == 6) {
            var return_expired_date: Date = Date()
            if (complete_at.isNotEmpty()) {
                complete_at.toDate("yyyy-MM-dd")?.let {
                    val c: Calendar = Calendar.getInstance()
                    c.time = it
                    c.add(Calendar.DATE, 10)
                    return_expired_date = c.time
                }
            }

            canReturn = (Date().before(return_expired_date) then { true }) ?: false
        } else {
            canReturn = false
        }
    }

    private fun makeAttributes(): String {
//
//        var attributes: ArrayList<String> = arrayListOf()
//        if (product_type == "clothes") {
//            for (item in order_clothes) {
//                val color: String = item.color
//                val size: String = item.size
//                unit = item.unit
//                val quantity: String = item.quantity.toString() + unit
//                val price: String = item.price.toString()
//                val attribute =
//                "顏色：" + color + "," +
//                        "尺寸：" + size + "," +
//                        "數量：" + quantity + "," +
//                        "價格：" + price
//                attributes.add(attribute)
//            }
//        } else if (product_type == "racket") {
//            for (item in order_racket) {
//                val color: String = item.color
//                val weight: String = item.weight
//                unit = item.unit
//                val quantity: String = item.quantity.toString() + unit
//                val price: String = item.price.toString()
//                val attribute =
//                "顏色：" + color + "," +
//                        "重量：" + weight + "," +
//                        "數量：" + quantity + "," +
//                        "價格：" + price
//                attributes.add(attribute)
//            }
//        } else if (product_type == "mejump") {
//            for (item in order_mejump) {
//                val title: String = item.title
//                unit = item.unit
//                val quantity: String = item.quantity.toString() + unit
//                val price: String = item.price.toString()
//                val attribute =
//                "種類：" + title + "," +
//                        "數量：" + quantity + "," +
//                        "價格：" + price
//                attributes.add(attribute)
//            }
//        }
//        val attribute: String = attributes.joinToString("\n")
//
        return ""
    }

    private fun thousandNumber(m: Int): String {
        val tmp = m.formattedWithSeparator()
        val price: String = "NT$ ${tmp}"

        return price
    }

//    private fun getProductPriceID(): Int {
//
//        var price_id: Int = 0
//        if (product_type == "clothes") {
//            for (tmp in order_clothes) {
//                price_id = tmp.price_id
//            }
//        } else if (product_type == "racket") {
//            for (tmp in order_racket) {
//                price_id = tmp.price_id
//            }
//        } else if (product_type == "mejump") {
//            for (tmp in order_mejump) {
//                price_id = tmp.price_id
//            }
//        }
//        return price_id
//    }

    private fun getProductPrice(): Int {
        //val price_id: Int = getProductPriceID()

        val product_price: Int = 9999999
//        if (product != null && product!!.prices.size > 0) {
//            val prices: ArrayList<ProductPriceTable> = product!!.prices
//            product_price = prices[0].price_member
////            for price in prices {
////                if price.id == price_id {
////                    product_price = price.price_member
////                    break
////                }
////            }
//        }
        return product_price
    }
}

class OrderItemTable: Table() {

    var order_id: Int = 0
    var product_id: Int = 0
    var attribute: String = ""
    var amount: Int = 0
    var discount: Int = 0
    var quantity: Int = 0
    var product: ProductTable? = null

    //[name: 尺寸]
    //[alias: size]
    //[value: M]
    var attributes: ArrayList<HashMap<String, String>> = arrayListOf()

    var amount_show: String = ""

    override fun filterRow() {
        super.filterRow()

        product?.filterRow()

        //{name:尺寸,alias:size,value:M}|{name:顏色,alias:color,value:藍色}
        val tmps: Array<String> = attribute.split("|").toTypedArray()

        for (tmp in tmps) {

            //{name:尺寸,alias:size,value:M}
            var _tmp = tmp.replace("{", "")
            _tmp = _tmp.replace("}", "")

            //name:尺寸,alias:size,value:M
            val arr: Array<String> = _tmp.split(",").toTypedArray()

            //name:尺寸
            //alias:size
            //value:M
            val a: HashMap<String, String> = hashMapOf()
            if (a.size > 0) {
                for (str in arr) {
                    val b: Array<String> = str.split(":").toTypedArray()
                    a[b[0]] = b[1]
                }

                attributes.add(a)
            }
        }

        if (amount > 0) {
            amount_show = "NT$ ${amount.formattedWithSeparator()} 元"
        } else {
            amount_show = "未提供"
        }
    }
}

class GatewayTable: Table() {

    var order_id: Int = 0
    var method: String = ""
    var process: String = ""
    var card6No: String = ""
    var card4No: String = ""
    var pay_from: String = ""
    var payment_no: String = ""
    var payment_url: String = ""
    var barcode1: String = ""
    var barcode2: String = ""
    var barcode3: String = ""
    var bank_code: String = ""
    var bank_account: String = ""
    var gateway_at: String = ""
    var expire_at: String = ""

    var method_show: String = ""
    var process_show: String = ""
    var gateway_at_show: String = ""
    var expire_at_show: String = ""

    override fun filterRow() {
        super.filterRow()

        gateway_at_show = if (gateway_at == null) { GATEWAY_PROCESS.getRawValueFromString("normal")} else { gateway_at.noSec()}
        expire_at_show = if (expire_at == null) { "" } else { expire_at.noSec() }
        method_show = GATEWAY.getRawValueFromString(method)
        process_show = GATEWAY_PROCESS.getRawValueFromString(process)

        if (gateway_at == null) { gateway_at = "" }
        if (card6No == null) { card6No = "" }
        if (card4No == null) { card4No = "" }
    }
}

class ShippingTable: Table() {

    var order_id: Int = -1
    var method: String = ""
    var store_id: Int = 0
    var store_name: String = ""
    var store_address: String = ""
    var store_tel: String = ""
    var outside: Int = 0
    var process: String = ""
    var RtnCode: Int = 0
    var RtnMsg: String = ""
    var AllPayLogisticsID: String = ""
    var amount: Int = 0

    var shipping_at: String = ""
    var store_at: String = ""
    var complete_at: String = ""
    var back_at: String = ""

    var UpdateStatusDate: String = ""
    var CVSPaymentNo: String = ""
    var CVSValidationNo: String = ""
    var BookingNote: String = ""

    var method_show: String = ""
    var process_show: String = ""
    var shipping_at_show: String = ""
    var store_at_show: String = ""
    var complete_at_show: String = ""
    var back_at_show: String = ""

    override fun filterRow() {
        super.filterRow()

        method_show = SHIPPING.getRawValueFromString(method)
        process_show = SHIPPING_PROCESS.getRawValueFromString(process)

        back_at = if (back_at == null) { "" } else { back_at }

        shipping_at_show = if (shipping_at == null) { SHIPPING.getRawValueFromString("direct")} else { shipping_at.noSec()}
        store_at_show = if (shipping_at == null) { "" } else { store_at.noSec()}
        complete_at_show = if (complete_at == null) { "" } else { complete_at.noSec()}
        back_at_show = if (back_at == null) { "" } else { back_at.noSec()}

        if (shipping_at == null) { shipping_at = "" }
        if (complete_at == null) { complete_at = "" }
    }
}

//class OrderClothesTable: Table() {
//
//    var order_id: Int = -1
//    var product_id: Int = -1
//    var price_id: Int = -1
//    var color: String = ""
//    var size: String = ""
//    var quantity: Int = -1
//    var price: Int = -1
//    var unit: String = "件"
//}
//
//class OrderRacketTable: Table() {
//
//    var order_id: Int = -1
//    var product_id: Int = -1
//    var price_id: Int = -1
//    var color: String = ""
//    var weight: String = ""
//    var quantity: Int = -1
//    var price: Int = -1
//    var unit: String = "隻"
//}
//
//class OrderMeJumpTable: Table() {
//
//    var order_id: Int = -1
//    var product_id: Int = -1
//    var price_id: Int = -1
//    var quantity: Int = -1
//    var price: Int = -1
//    var unit: String = "組"
//}