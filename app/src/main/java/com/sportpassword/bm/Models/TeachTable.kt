package com.sportpassword.bm.Models


class TeachesTable: Tables() {
    var rows: ArrayList<TeachTable> = arrayListOf()
}

class TeachTable: Table() {

    var youtube: String = ""
    var provider: String = ""
    var provider_url: String = ""

    override fun filterRow() {
        super.filterRow()
    }
}