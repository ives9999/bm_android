package com.sportpassword.bm.Models

class CoachesTable: Tables() {

    var rows: ArrayList<CoachTable> = arrayListOf()
}

class CoachTable: Table() {

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
    var manager_id: Int = -1
    var color: String = ""
    var citys: ArrayList<CityTable> = arrayListOf()

    var seniority_show: String = ""

    override fun filterRow() {
        super.filterRow()

        if (website == null) {
            website = "未提供"
        }

        if (line == null) {
            line = "未提供"
        }

        if (fb == null) {
            fb = "未提供"
        }

        if (youtube == null) {
            youtube = "未提供"
        }

        if (seniority >= 0) {
            seniority_show = "${seniority}年"
        }
    }
}