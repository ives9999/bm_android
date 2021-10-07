package com.sportpassword.bm.Data

data class AddCartSection (
    var title: String = "",
    var isExpanded: Boolean = true,
    var items: ArrayList<AddCartRow> = arrayListOf()
)
data class AddCartRow(
    var title: String = "",
    var value: String = "",
    var show: String = "",
    var key: String = "",
    var cell: String = ""
)
