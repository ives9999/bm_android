package com.sportpassword.bm.Models

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

    var UpdateStatusDate: String = ""
    var CVSPaymentNo: String = ""
    var CVSValidationNo: String = ""
    var BookingNote: String = ""

    override fun filterRow() {
        super.filterRow()
    }
}