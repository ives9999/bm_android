package com.sportpassword.bm.Data

data class TeamSearchSection (
    var title: String = "",
    var isExpanded: Boolean = true,
    var items: ArrayList<TeamSearchRow> = arrayListOf()
)

data class TeamSearchRow (
    var title: String = "",
    var value: String = "",
    var show: String = "",
    var key: String = "",
    var cell: String = ""
)
