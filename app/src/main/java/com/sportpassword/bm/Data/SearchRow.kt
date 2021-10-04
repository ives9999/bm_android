package com.sportpassword.bm.Data

data class SearchSection (
    var title: String = "",
    var isExpanded: Boolean = true,
    var items: ArrayList<SearchRow> = arrayListOf()
)

data class SearchRow (
    var title: String = "",
    var value: String = "",
    var show: String = "",
    var key: String = "",
    var cell: String = ""
)
