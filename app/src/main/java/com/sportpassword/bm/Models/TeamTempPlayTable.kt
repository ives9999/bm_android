package com.sportpassword.bm.Models

import com.google.gson.annotations.SerializedName

class TeamTempPlayTable: Table() {

    var team_id: Int = 0
    var member_id: Int = 0
    var play_date: String = ""
    @SerializedName("member") var memberTable: MemberTable? = null

    override fun filterRow() {
        super.filterRow()
        memberTable?.filterRow()
    }
}