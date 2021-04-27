package com.sportpassword.bm.Models

class PaymentTable: Table() {

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

    override fun filterRow() {
        super.filterRow()
    }
}