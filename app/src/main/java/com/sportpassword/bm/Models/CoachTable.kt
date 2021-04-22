package com.sportpassword.bm.Models

class CoachesTable: Tables() {
    var rows: ArrayList<CoachTable> = arrayListOf()
}

class CoachTable: Table() {
    var citys: ArrayList<SuperCity> = arrayListOf()
    var email: String = ""
    var website: String = ""
    var fb: String = ""
    var youtube: String = ""
    var line: String = ""
    var seniority: Int = -1
    var exp: String = ""
    var feat: String = ""
    var license: String = ""
    var charge: String = ""
    var content: String = ""
    var color: String = ""
    var manager_id: Int = 0

    var seniority_show: String = ""

    override fun filterRow() {

        super.filterRow()

        if (seniority >= 0) {
            seniority_show = "${seniority}年"
        }
    }
}