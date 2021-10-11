package com.sportpassword.bm.Data

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
    var cell: String = ""
)
