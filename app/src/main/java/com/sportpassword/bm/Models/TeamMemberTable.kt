package com.sportpassword.bm.Models

import com.google.gson.annotations.SerializedName

class TeamMemberTable: Table() {
    var team_id: Int = -1
    var member_id: Int = -1
//    var member_nickname: String = ""
//    var member_token: String = ""
//    var manager_nickname: String = ""
//    var team_name: String = ""

    @SerializedName("member") var memberTable: MemberTable? = null
    @SerializedName("manager") var managerTable: MemberTable? = null
    @SerializedName("team") var teamTable: TeamTable? = null

    override fun filterRow() {
        super.filterRow()
    }
}