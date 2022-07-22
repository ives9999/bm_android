package com.sportpassword.bm.Data

import com.sportpassword.bm.Models.Table
import com.sportpassword.bm.Utilities.KEYBOARD

data class OneSection (
    var title: String = "",
    var key: String = "",
    var isExpanded: Boolean = true,
    var items: ArrayList<OneRow> = arrayListOf()
)

data class OneRow(
    var title: String = "",
    var value: String = "",
    var show: String = "",
    var key: String = "",
    var cell: String = "",
    var keyboard: KEYBOARD = KEYBOARD.default,
    var placeholder: String = "",
    var prompt: String = "",
    var isRequired: Boolean = false,
    var isClear: Boolean = true,
    var feature_path: String = "",
    var attribute: String = "",
    var amount: String = "",
    var quantity: String = "",
    var titleColor: Int = 0,
    var showColor: Int = 0,
    var token: String = ""
) {
    var msg: String = ""
}

//data class OrderSection<T: Table> (
//    var title: String = "",
//    var key: String = "",
//    var isExpanded: Boolean = true,
//    var items: ArrayList<OrderRow<T>> = arrayListOf()
//)
//
//class OrderRow<T: Table>(
//    row: T,
//    key: String,
//    cell: String
//)
