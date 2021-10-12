package com.sportpassword.bm.Data

import com.sportpassword.bm.Models.Table

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
    var feature_path: String = "",
    var attribute: String = "",
    var amount: String = "",
    var quantity: String = ""
)

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
