package com.sportpassword.bm.Data

data class MemberSection (
    var title: String = "",
    var isExpanded: Boolean = true,
    var items: ArrayList<MemberRow> = arrayListOf()
)
{}

data class MemberRow (
    var title: String = "",
    var icon: String = ""
        )
{}
